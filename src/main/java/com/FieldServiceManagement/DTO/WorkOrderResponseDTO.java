
package com.FieldServiceManagement.DTO;

import java.time.LocalDateTime;

import com.FieldServiceManagement.Entity.WorkOrder;
import com.FieldServiceManagement.ENUM.WorkOrderPriority;
import com.FieldServiceManagement.ENUM.WorkOrderStatus;

public class WorkOrderResponseDTO {

    private Long id;
    private String workOrderNumber;
    private String title;
    private String description;

    private WorkOrderStatus status;
    private WorkOrderPriority priority;

    private Long customerId;
    private String customerName;

    private Long siteId;
    private String siteName;

    private Long assignedTechnicianId;
    private String assignedTechnicianName;

    private Long createdById;
    private String createdByName;

    private LocalDateTime scheduledAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public WorkOrderResponseDTO() {
    }

    public static WorkOrderResponseDTO from(WorkOrder wo) {
        WorkOrderResponseDTO dto = new WorkOrderResponseDTO();

        dto.setId(wo.getId());
        dto.setWorkOrderNumber(wo.getWorkOrderNumber());
        dto.setTitle(wo.getTitle());
        dto.setDescription(wo.getDescription());
        dto.setStatus(wo.getStatus());
        dto.setPriority(wo.getPriority());

        dto.setCustomerId(wo.getCustomer().getId());
        dto.setCustomerName(wo.getCustomer().getCompanyName());

        dto.setSiteId(wo.getSite().getId());
        dto.setSiteName(wo.getSite().getSiteName());

        if (wo.getAssignedTechnician() != null) {
            dto.setAssignedTechnicianId(
                    wo.getAssignedTechnician().getId());
            dto.setAssignedTechnicianName(
                    wo.getAssignedTechnician().getUserName());
        }

        dto.setCreatedById(wo.getCreatedBy().getId());
        dto.setCreatedByName(wo.getCreatedBy().getUserName());

        dto.setScheduledAt(wo.getScheduledAt());
        dto.setStartedAt(wo.getStartedAt());
        dto.setCompletedAt(wo.getCompletedAt());
        dto.setCreatedAt(wo.getCreatedAt());
        dto.setUpdatedAt(wo.getUpdatedAt());

        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getWorkOrderNumber() { return workOrderNumber; }
    public void setWorkOrderNumber(String value) { this.workOrderNumber = value; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public WorkOrderStatus getStatus() { return status; }
    public void setStatus(WorkOrderStatus status) { this.status = status; }

    public WorkOrderPriority getPriority() { return priority; }
    public void setPriority(WorkOrderPriority priority) { this.priority = priority; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long value) { this.customerId = value; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String value) { this.customerName = value; }

    public Long getSiteId() { return siteId; }
    public void setSiteId(Long value) { this.siteId = value; }

    public String getSiteName() { return siteName; }
    public void setSiteName(String value) { this.siteName = value; }

    public Long getAssignedTechnicianId() { return assignedTechnicianId; }
    public void setAssignedTechnicianId(Long value) { this.assignedTechnicianId = value; }

    public String getAssignedTechnicianName() { return assignedTechnicianName; }
    public void setAssignedTechnicianName(String value) { this.assignedTechnicianName = value; }

    public Long getCreatedById() { return createdById; }
    public void setCreatedById(Long value) { this.createdById = value; }

    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String value) { this.createdByName = value; }

    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(LocalDateTime value) { this.scheduledAt = value; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime value) { this.startedAt = value; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime value) { this.completedAt = value; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime value) { this.createdAt = value; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime value) { this.updatedAt = value; }
}
