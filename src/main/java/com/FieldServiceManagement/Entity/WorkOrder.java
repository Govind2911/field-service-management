package com.FieldServiceManagement.Entity;


import java.time.LocalDateTime;
import java.util.UUID;

import com.FieldServiceManagement.ENUM.WorkOrderPriority;
import com.FieldServiceManagement.ENUM.WorkOrderStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "work_orders")
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String workOrderNumber;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 3000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private WorkOrderStatus status = WorkOrderStatus.OPEN;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private WorkOrderPriority priority = WorkOrderPriority.MEDIUM;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_technician_id")
    private UserAuth assignedTechnician;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by_id", nullable = false)
    private UserAuth createdBy;

    private LocalDateTime scheduledAt;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void beforeInsert() {
        if (workOrderNumber == null || workOrderNumber.isBlank()) {
            workOrderNumber = "WO-" +
                    UUID.randomUUID().toString()
                            .substring(0, 8).toUpperCase();
        }

        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void beforeUpdate() {
        updatedAt = LocalDateTime.now();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWorkOrderNumber() {
		return workOrderNumber;
	}

	public void setWorkOrderNumber(String workOrderNumber) {
		this.workOrderNumber = workOrderNumber;
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

	public WorkOrderStatus getStatus() {
		return status;
	}

	public void setStatus(WorkOrderStatus status) {
		this.status = status;
	}

	public WorkOrderPriority getPriority() {
		return priority;
	}

	public void setPriority(WorkOrderPriority priority) {
		this.priority = priority;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public UserAuth getAssignedTechnician() {
		return assignedTechnician;
	}

	public void setAssignedTechnician(UserAuth assignedTechnician) {
		this.assignedTechnician = assignedTechnician;
	}

	public UserAuth getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserAuth createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getScheduledAt() {
		return scheduledAt;
	}

	public void setScheduledAt(LocalDateTime scheduledAt) {
		this.scheduledAt = scheduledAt;
	}

	public LocalDateTime getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(LocalDateTime startedAt) {
		this.startedAt = startedAt;
	}

	public LocalDateTime getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(LocalDateTime completedAt) {
		this.completedAt = completedAt;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
    
}