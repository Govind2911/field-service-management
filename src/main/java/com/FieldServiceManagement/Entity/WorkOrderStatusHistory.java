
package com.FieldServiceManagement.Entity;

import java.time.LocalDateTime;

import com.FieldServiceManagement.ENUM.WorkOrderStatus;

import jakarta.persistence.*;

@Entity
@Table(name = "work_order_status_history")
public class WorkOrderStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "work_order_id", nullable = false)
    private WorkOrder workOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status", length = 30)
    private WorkOrderStatus previousStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false, length = 30)
    private WorkOrderStatus newStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "changed_by_id", nullable = false)
    private UserAuth changedBy;

    @Column(length = 500)
    private String remarks;

    @Column(nullable = false, updatable = false)
    private LocalDateTime changedAt;

    public WorkOrderStatusHistory() {
    }

    @PrePersist
    public void beforeInsert() {
        if (changedAt == null) {
            changedAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public WorkOrderStatus getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(WorkOrderStatus previousStatus) {
        this.previousStatus = previousStatus;
    }

    public WorkOrderStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(WorkOrderStatus newStatus) {
        this.newStatus = newStatus;
    }

    public UserAuth getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(UserAuth changedBy) {
        this.changedBy = changedBy;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }
}
