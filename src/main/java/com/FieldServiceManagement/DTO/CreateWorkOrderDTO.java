
package com.FieldServiceManagement.DTO;

import java.time.LocalDateTime;

import com.FieldServiceManagement.ENUM.WorkOrderPriority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateWorkOrderDTO {

    @NotBlank
    @Size(max = 150)
    private String title;

    @NotBlank
    @Size(max = 3000)
    private String description;

    @NotNull
    private Long customerId;

    @NotNull
    private Long siteId;

    private WorkOrderPriority priority = WorkOrderPriority.MEDIUM;

    private LocalDateTime scheduledAt;

    public CreateWorkOrderDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public WorkOrderPriority getPriority() {
        return priority;
    }

    public void setPriority(WorkOrderPriority priority) {
        this.priority = priority;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }
}
