package com.FieldServiceManagement.Service;
import com.FieldServiceManagement.DTO.EmailLogDTO;
import com.FieldServiceManagement.Security.EmailLogService;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.FieldServiceManagement.DTO.SlaResponseDTO;
import com.FieldServiceManagement.Entity.UserAuth;
import com.FieldServiceManagement.Entity.WorkOrder;
import com.FieldServiceManagement.ENUM.Role;
import com.FieldServiceManagement.ENUM.WorkOrderStatus;
import com.FieldServiceManagement.Repository.UserAuthRepository;
import com.FieldServiceManagement.Repository.WorkOrderRepository;

@Service
public class SlaService {

    private final WorkOrderRepository workOrderRepository;
    private final UserAuthRepository userAuthRepository;
    private final EmailLogService emailLogService;
    
    public SlaService(
            WorkOrderRepository workOrderRepository,
            UserAuthRepository userAuthRepository,
            EmailLogService emailLogService) {

        this.workOrderRepository = workOrderRepository;
        this.userAuthRepository = userAuthRepository;
        this.emailLogService = emailLogService;
    }

    private UserAuth getUser(String email) {
        return userAuthRepository.findByUserEmail(email)
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "Authenticated user not found"));
    }

    private void requireManagerOrDispatcher(String email) {
        UserAuth user = getUser(email);

        if (user.getRole() != Role.MANAGER
                && user.getRole() != Role.DISPATCHER) {
            throw new IllegalArgumentException(
                "Only MANAGER or DISPATCHER can access SLA reports");
        }
    }

    private boolean isActive(WorkOrder order) {
        return order.getStatus() != WorkOrderStatus.COMPLETED
                && order.getStatus() != WorkOrderStatus.CLOSED
                && order.getStatus() != WorkOrderStatus.CANCELLED;
    }

    // Checks overdue work orders every minute.

@Scheduled(fixedRate = 60000)
@Transactional
public void checkSlaBreaches() {
	System.out.println("SLA SCHEDULER RUNNING: " + LocalDateTime.now());
    List<WorkOrder> breachedOrders = new java.util.ArrayList<>();
    LocalDateTime now = LocalDateTime.now();

    for (WorkOrder order : workOrderRepository.findAll()) {
        if (isActive(order)
                && !order.isSlaBreached()
                && order.getSlaDueAt() != null
                && order.getSlaDueAt().isBefore(now)) {

            order.setSlaBreached(true);
            order.setSlaBreachedAt(now);
            breachedOrders.add(order);
        }
    }
    System.out.println("NEW BREACHED ORDERS: " + breachedOrders.size());
    if (breachedOrders.isEmpty()) {
        return;
    }

    // Save breach status before sending notifications.
    workOrderRepository.saveAll(breachedOrders);

    // Find all managers and dispatchers.
    List<UserAuth> recipients = userAuthRepository.findAll()
            .stream()
            .filter(user -> user.getRole() == Role.MANAGER
                    || user.getRole() == Role.DISPATCHER)
            .filter(user -> user.getUserEmail() != null
                    && !user.getUserEmail().isBlank())
            .toList();

    // Notify only for newly detected breaches.
    for (WorkOrder order : breachedOrders) {
        String technician = order.getAssignedTechnician() != null
                ? order.getAssignedTechnician().getUserName()
                : "Not assigned";

        String subject = "SLA Breach Alert - "
                + order.getWorkOrderNumber();

        String body = "<html><body>"
                + "<h2>SLA Breach Alert</h2>"
                + "<p>Work Order: " + order.getWorkOrderNumber() + "</p>"
                + "<p>Title: " + order.getTitle() + "</p>"
                + "<p>Priority: " + order.getPriority() + "</p>"
                + "<p>Status: " + order.getStatus() + "</p>"
                + "<p>SLA Deadline: " + order.getSlaDueAt() + "</p>"
                + "<p>Assigned Technician: " + technician + "</p>"
                + "<p>Breach Detected At: "
                + order.getSlaBreachedAt() + "</p>"
                + "</body></html>";

        for (UserAuth recipient : recipients) {
            try {
                EmailLogDTO email = new EmailLogDTO();
                email.recepientEmail = recipient.getUserEmail();
                email.subject = subject;
                email.body = body;

                // Reuse the existing email and logging system.
                System.out.println("SENDING SLA EMAIL TO: " + recipient.getUserEmail());
                String result = emailLogService.notify(email);
                System.out.println(
                        "SLA notification for "
                        + order.getWorkOrderNumber()
                        + " to " + recipient.getUserEmail()
                        + ": " + result);

            } catch (Exception ex) {
                System.err.println(
                        "SLA notification failed for "
                        + order.getWorkOrderNumber()
                        + ": " + ex.getMessage());
            }
        }
    }
}


    // Lists active work orders whose SLA deadline has passed.
    @Transactional(readOnly = true)
    public List<SlaResponseDTO> getBreachedOrders(String email) {
        requireManagerOrDispatcher(email);

        return workOrderRepository.findAll()
                .stream()
                .filter(this::isActive)
                .filter(order -> order.isSlaBreached()
                        || (order.getSlaDueAt() != null
                            && order.getSlaDueAt().isBefore(
                                LocalDateTime.now())))
                .map(SlaResponseDTO::from)
                .toList();
    }

    // Shows SLA details for a particular work order.
    @Transactional(readOnly = true)
    public SlaResponseDTO getSlaDetails(Long id, String email) {
        requireManagerOrDispatcher(email);

        WorkOrder order = workOrderRepository.findById(id)
                .orElseThrow(() ->
                    new IllegalArgumentException("Work order not found"));

        return SlaResponseDTO.from(order);
    }
}