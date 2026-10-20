package com.FieldServiceManagement.DTO;

import java.time.LocalDateTime;

import com.FieldServiceManagement.Entity.WorkOrder;
import com.FieldServiceManagement.ENUM.WorkOrderPriority;
import com.FieldServiceManagement.ENUM.WorkOrderStatus;

public class SlaResponseDTO {

    private Long workOrderId;
    private String workOrderNumber;
    private String title;
    private WorkOrderPriority priority;
    private WorkOrderStatus status;
    private LocalDateTime slaDueAt;
    private boolean slaBreached;
    private LocalDateTime slaBreachedAt;
    private String assignedTechnicianName;

    public static SlaResponseDTO from(WorkOrder order) {
        SlaResponseDTO dto = new SlaResponseDTO();

        dto.setWorkOrderId(order.getId());
        dto.setWorkOrderNumber(order.getWorkOrderNumber());
        dto.setTitle(order.getTitle());
        dto.setPriority(order.getPriority());
        dto.setStatus(order.getStatus());
        dto.setSlaDueAt(order.getSlaDueAt());
        dto.setSlaBreached(order.isSlaBreached());
        dto.setSlaBreachedAt(order.getSlaBreachedAt());

        if (order.getAssignedTechnician() != null) {
            dto.setAssignedTechnicianName(
                order.getAssignedTechnician().getUserName()
            );
        }

        return dto;
    }

    public Long getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(Long value) { this.workOrderId = value; }

    public String getWorkOrderNumber() { return workOrderNumber; }
    public void setWorkOrderNumber(String value) { this.workOrderNumber = value; }

    public String getTitle() { return title; }
    public void setTitle(String value) { this.title = value; }

    public WorkOrderPriority getPriority() { return priority; }
    public void setPriority(WorkOrderPriority value) { this.priority = value; }

    public WorkOrderStatus getStatus() { return status; }
    public void setStatus(WorkOrderStatus value) { this.status = value; }

    public LocalDateTime getSlaDueAt() { return slaDueAt; }
    public void setSlaDueAt(LocalDateTime value) { this.slaDueAt = value; }

    public boolean isSlaBreached() { return slaBreached; }
    public void setSlaBreached(boolean value) { this.slaBreached = value; }

    public LocalDateTime getSlaBreachedAt() { return slaBreachedAt; }
    public void setSlaBreachedAt(LocalDateTime value) {
        this.slaBreachedAt = value;
    }

    public String getAssignedTechnicianName() {
        return assignedTechnicianName;
    }

    public void setAssignedTechnicianName(String value) {
        this.assignedTechnicianName = value;
    }
}