package com.FieldServiceManagement.Service;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.FieldServiceManagement.DTO.*;
import com.FieldServiceManagement.Entity.*;
import com.FieldServiceManagement.ENUM.Role;
import com.FieldServiceManagement.ENUM.WorkOrderStatus;
import com.FieldServiceManagement.Repository.*;

@Service
public class PartsTimeService {

    private final PartRepository parts;
    private final WorkOrderPartUsageRepository usages;
    private final WorkOrderTimeLogRepository timeLogs;
    private final WorkOrderRepository workOrders;
    private final UserAuthRepository users;

    public PartsTimeService(
            PartRepository parts,
            WorkOrderPartUsageRepository usages,
            WorkOrderTimeLogRepository timeLogs,
            WorkOrderRepository workOrders,
            UserAuthRepository users) {
        this.parts = parts;
        this.usages = usages;
        this.timeLogs = timeLogs;
        this.workOrders = workOrders;
        this.users = users;
    }

    private UserAuth getUser(String email) {
        return users.findByUserEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("Authenticated user not found"));
    }

    private WorkOrder getWorkOrder(Long id) {
        return workOrders.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Work order not found"));
    }

    private void checkInventoryPermission(UserAuth user) {
        if (user.getRole() != Role.MANAGER
                && user.getRole() != Role.DISPATCHER) {
            throw new IllegalArgumentException(
                    "Only MANAGER or DISPATCHER can manage inventory");
        }
    }

    private void checkWorkOrderAccess(UserAuth user, WorkOrder order) {
        if (user.getRole() == Role.TECHNICIAN) {
            if (order.getAssignedTechnician() == null
                    || !order.getAssignedTechnician().getId()
                            .equals(user.getId())) {
                throw new IllegalArgumentException(
                        "You can access only work orders assigned to you");
            }
        } else if (user.getRole() != Role.MANAGER
                && user.getRole() != Role.DISPATCHER) {
            throw new IllegalArgumentException(
                    "You are not allowed to log parts or time");
        }

        if (order.getStatus() == WorkOrderStatus.CLOSED
                || order.getStatus() == WorkOrderStatus.CANCELLED) {
            throw new IllegalArgumentException(
                    "Cannot add entries to closed or cancelled work orders");
        }
    }

    @Transactional
    public Map<String, Object> createPart(CreatePartDTO dto, String email) {
        checkInventoryPermission(getUser(email));

        String number = dto.getPartNumber().trim();
        String name = dto.getName().trim();

        if (number.isEmpty() || name.isEmpty()) {
            throw new IllegalArgumentException(
                    "Part number and name are required");
        }

        if (dto.getStockQuantity() == null || dto.getStockQuantity() < 0) {
            throw new IllegalArgumentException(
                    "Stock quantity cannot be negative");
        }

        if (dto.getUnitCost() == null
                || dto.getUnitCost().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Unit cost cannot be negative");
        }

        if (parts.findByPartNumber(number).isPresent()) {
            throw new IllegalArgumentException(
                    "Part number already exists");
        }

        Part part = new Part();
        part.setPartNumber(number);
        part.setName(name);
        part.setDescription(dto.getDescription());
        part.setStockQuantity(dto.getStockQuantity());
        part.setUnitCost(dto.getUnitCost());

        return partView(parts.save(part));
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> listParts() {
        return parts.findAll().stream()
                .map(this::partView)
                .toList();
    }

    @Transactional
    public Map<String, Object> addStock(
            Long id, Integer quantity, String email) {

        checkInventoryPermission(getUser(email));

        if (quantity == null || quantity < 1) {
            throw new IllegalArgumentException(
                    "Quantity must be at least 1");
        }

        Part part = parts.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Part not found"));

        try {
            part.setStockQuantity(
                    Math.addExact(part.getStockQuantity(), quantity));
        } catch (ArithmeticException ex) {
            throw new IllegalArgumentException("Stock quantity is too large");
        }

        return partView(parts.save(part));
    }

    @Transactional
    public Map<String, Object> usePart(
            Long workOrderId, UsePartDTO dto, String email) {

        UserAuth actor = getUser(email);
        WorkOrder order = getWorkOrder(workOrderId);
        checkWorkOrderAccess(actor, order);

        if (dto.getPartId() == null
                || dto.getQuantity() == null
                || dto.getQuantity() < 1) {
            throw new IllegalArgumentException(
                    "Part ID and a positive quantity are required");
        }

        Part part = parts.findByIdForUpdate(dto.getPartId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Part not found"));

        if (part.getStockQuantity() < dto.getQuantity()) {
            throw new IllegalArgumentException(
                    "Insufficient stock. Available quantity: "
                            + part.getStockQuantity());
        }

        part.setStockQuantity(
                part.getStockQuantity() - dto.getQuantity());
        parts.save(part);

        WorkOrderPartUsage usage = new WorkOrderPartUsage();
        usage.setWorkOrder(order);
        usage.setPart(part);
        usage.setUsedBy(actor);
        usage.setQuantity(dto.getQuantity());
        usage.setUnitCost(part.getUnitCost());
        usage.setNote(dto.getNote());

        return usageView(usages.save(usage));
    }

    @Transactional
    public Map<String, Object> logTime(
            Long workOrderId, CreateTimeLogDTO dto, String email) {

        UserAuth actor = getUser(email);
        WorkOrder order = getWorkOrder(workOrderId);
        checkWorkOrderAccess(actor, order);

        if (dto.getMinutes() == null
                || dto.getMinutes() < 1
                || dto.getMinutes() > 1440) {
            throw new IllegalArgumentException(
                    "Minutes must be between 1 and 1440");
        }

        WorkOrderTimeLog log = new WorkOrderTimeLog();
        log.setWorkOrder(order);
        log.setTechnician(actor);
        log.setMinutes(dto.getMinutes());
        log.setNote(dto.getNote());

        return timeView(timeLogs.save(log));
    }

    @Transactional(readOnly = true)
    public Map<String, Object> summary(Long workOrderId, String email) {
        UserAuth actor = getUser(email);
        WorkOrder order = getWorkOrder(workOrderId);

        if (actor.getRole() == Role.CUSTOMER) {
            throw new IllegalArgumentException(
                    "Customers cannot access internal parts/time logs");
        }

        if (actor.getRole() == Role.TECHNICIAN
                && (order.getAssignedTechnician() == null
                || !order.getAssignedTechnician().getId()
                        .equals(actor.getId()))) {
            throw new IllegalArgumentException(
                    "You can view only work orders assigned to you");
        }

        List<WorkOrderPartUsage> partUsages =
                usages.findByWorkOrderId(workOrderId);

        List<WorkOrderTimeLog> logs =
                timeLogs.findByWorkOrderId(workOrderId);

        BigDecimal partsCost = partUsages.stream()
                .map(u -> u.getUnitCost()
                        .multiply(BigDecimal.valueOf(u.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalMinutes = logs.stream()
                .mapToInt(WorkOrderTimeLog::getMinutes)
                .sum();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("workOrderId", workOrderId);
        result.put("partsUsage",
                partUsages.stream().map(this::usageView).toList());
        result.put("timeLogs",
                logs.stream().map(this::timeView).toList());
        result.put("totalPartsCost", partsCost);
        result.put("totalLabourMinutes", totalMinutes);
        result.put("totalLabourHours",
                BigDecimal.valueOf(totalMinutes)
                        .divide(BigDecimal.valueOf(60), 2,
                                RoundingMode.HALF_UP));

        return result;
    }

    private Map<String, Object> partView(Part part) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", part.getId());
        result.put("partNumber", part.getPartNumber());
        result.put("name", part.getName());
        result.put("description", part.getDescription());
        result.put("stockQuantity", part.getStockQuantity());
        result.put("unitCost", part.getUnitCost());
        return result;
    }

    private Map<String, Object> usageView(WorkOrderPartUsage usage) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", usage.getId());
        result.put("workOrderId", usage.getWorkOrder().getId());
        result.put("partId", usage.getPart().getId());
        result.put("partNumber", usage.getPart().getPartNumber());
        result.put("partName", usage.getPart().getName());
        result.put("quantity", usage.getQuantity());
        result.put("unitCost", usage.getUnitCost());
        result.put("totalCost",
                usage.getUnitCost()
                        .multiply(BigDecimal.valueOf(usage.getQuantity())));
        result.put("usedBy", usage.getUsedBy().getUserName());
        result.put("usedAt", usage.getUsedAt());
        result.put("note", usage.getNote());
        return result;
    }

    private Map<String, Object> timeView(WorkOrderTimeLog log) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", log.getId());
        result.put("workOrderId", log.getWorkOrder().getId());
        result.put("technicianId", log.getTechnician().getId());
        result.put("technicianName", log.getTechnician().getUserName());
        result.put("minutes", log.getMinutes());
        result.put("note", log.getNote());
        result.put("loggedAt", log.getLoggedAt());
        return result;
    }
}