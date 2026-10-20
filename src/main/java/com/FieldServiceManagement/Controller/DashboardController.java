package com.FieldServiceManagement.Controller;


import java.security.Principal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.FieldServiceManagement.Entity.UserAuth;
import com.FieldServiceManagement.Entity.WorkOrder;
import com.FieldServiceManagement.ENUM.Role;
import com.FieldServiceManagement.ENUM.WorkOrderStatus;
import com.FieldServiceManagement.Repository.UserAuthRepository;
import com.FieldServiceManagement.Repository.WorkOrderRepository;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final WorkOrderRepository workOrderRepository;
    private final UserAuthRepository userAuthRepository;

    public DashboardController(
            WorkOrderRepository workOrderRepository,
            UserAuthRepository userAuthRepository) {
        this.workOrderRepository = workOrderRepository;
        this.userAuthRepository = userAuthRepository;
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getSummary(Principal principal) {

        UserAuth user = userAuthRepository
                .findByUserEmail(principal.getName())
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        if (user.getRole() != Role.MANAGER
                && user.getRole() != Role.DISPATCHER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message",
                            "Only Manager or Dispatcher can view dashboard"));
        }

        List<WorkOrder> orders = workOrderRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        Map<String, Long> statusCounts = new LinkedHashMap<>();

        for (WorkOrderStatus status : WorkOrderStatus.values()) {
            statusCounts.put(
                    status.name(),
                    orders.stream()
                            .filter(o -> o.getStatus() == status)
                            .count());
        }

        long overdueOrders = orders.stream()
                .filter(o -> o.getSlaDueAt() != null)
                .filter(o -> o.getSlaDueAt().isBefore(now))
                .filter(o -> !o.isSlaBreached())
                .filter(o -> o.getStatus() != WorkOrderStatus.COMPLETED)
                .filter(o -> o.getStatus() != WorkOrderStatus.CLOSED)
                .filter(o -> o.getStatus() != WorkOrderStatus.CANCELLED)
                .count();

        long breachedOrders = orders.stream()
                .filter(WorkOrder::isSlaBreached)
                .count();

        List<WorkOrder> completedWithSla = orders.stream()
                .filter(o -> o.getStatus() == WorkOrderStatus.COMPLETED)
                .filter(o -> o.getSlaDueAt() != null)
                .filter(o -> o.getCompletedAt() != null)
                .toList();

        long slaMet = completedWithSla.stream()
                .filter(o -> !o.getCompletedAt().isAfter(o.getSlaDueAt()))
                .count();

        double slaCompliance = completedWithSla.isEmpty()
                ? 0.0
                : Math.round(
                        (slaMet * 100.0 / completedWithSla.size()) * 100.0
                  ) / 100.0;

        Map<String, Long> technicianBreakdown = orders.stream()
                .collect(Collectors.groupingBy(
                        o -> o.getAssignedTechnician() == null
                                ? "Unassigned"
                                : o.getAssignedTechnician().getUserName(),
                        LinkedHashMap::new,
                        Collectors.counting()));

        Map<String, Long> siteBreakdown = orders.stream()
                .collect(Collectors.groupingBy(
                        o -> o.getSite() == null
                                ? "Unknown"
                                : o.getSite().getSiteName(),
                        LinkedHashMap::new,
                        Collectors.counting()));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalWorkOrders", orders.size());
        result.put("statusCounts", statusCounts);
        result.put("overdueOrders", overdueOrders);
        result.put("breachedOrders", breachedOrders);
        result.put("slaCompliancePercentage", slaCompliance);
        result.put("technicianBreakdown", technicianBreakdown);
        result.put("siteBreakdown", siteBreakdown);
        result.put("generatedAt", now);

        return ResponseEntity.ok(result);
    }
}