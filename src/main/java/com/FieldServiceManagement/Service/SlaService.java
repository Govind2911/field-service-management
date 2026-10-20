package com.FieldServiceManagement.Service;


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

    public SlaService(
            WorkOrderRepository workOrderRepository,
            UserAuthRepository userAuthRepository) {
        this.workOrderRepository = workOrderRepository;
        this.userAuthRepository = userAuthRepository;
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

    	workOrderRepository.saveAll(breachedOrders);
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