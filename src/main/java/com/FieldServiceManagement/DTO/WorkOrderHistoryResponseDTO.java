
package com.FieldServiceManagement.DTO;

import java.time.LocalDateTime;

import com.FieldServiceManagement.Entity.WorkOrderStatusHistory;
import com.FieldServiceManagement.ENUM.WorkOrderStatus;

public class WorkOrderHistoryResponseDTO {

    private Long id;
    private WorkOrderStatus previousStatus;
    private WorkOrderStatus newStatus;
    private Long changedById;
    private String changedByName;
    private String remarks;
    private LocalDateTime changedAt;

    public WorkOrderHistoryResponseDTO() {
    }

    public static WorkOrderHistoryResponseDTO from(
            WorkOrderStatusHistory history) {

        WorkOrderHistoryResponseDTO dto =
                new WorkOrderHistoryResponseDTO();

        dto.setId(history.getId());
        dto.setPreviousStatus(history.getPreviousStatus());
        dto.setNewStatus(history.getNewStatus());

        if (history.getChangedBy() != null) {
            dto.setChangedById(history.getChangedBy().getId());
            dto.setChangedByName(
                    history.getChangedBy().getUserName());
        }

        dto.setRemarks(history.getRemarks());
        dto.setChangedAt(history.getChangedAt());

        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public WorkOrderStatus getPreviousStatus() { return previousStatus; }
    public void setPreviousStatus(WorkOrderStatus value) { this.previousStatus = value; }

    public WorkOrderStatus getNewStatus() { return newStatus; }
    public void setNewStatus(WorkOrderStatus value) { this.newStatus = value; }

    public Long getChangedById() { return changedById; }
    public void setChangedById(Long value) { this.changedById = value; }

    public String getChangedByName() { return changedByName; }
    public void setChangedByName(String value) { this.changedByName = value; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public LocalDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalDateTime value) { this.changedAt = value; }
}
