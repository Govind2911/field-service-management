package com.FieldServiceManagement.Entity;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "work_order_part_usage")
public class WorkOrderPartUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "work_order_id", nullable = false)
    private WorkOrder workOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "part_id", nullable = false)
    private Part part;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "used_by_id", nullable = false)
    private UserAuth usedBy;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal unitCost;

    @Column(nullable = false)
    private LocalDateTime usedAt;

    @Column(length = 500)
    private String note;

    @PrePersist
    public void onCreate() {
        if (usedAt == null) usedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long v) { id = v; }
    public WorkOrder getWorkOrder() { return workOrder; }
    public void setWorkOrder(WorkOrder v) { workOrder = v; }
    public Part getPart() { return part; }
    public void setPart(Part v) { part = v; }
    public UserAuth getUsedBy() { return usedBy; }
    public void setUsedBy(UserAuth v) { usedBy = v; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer v) { quantity = v; }
    public BigDecimal getUnitCost() { return unitCost; }
    public void setUnitCost(BigDecimal v) { unitCost = v; }
    public LocalDateTime getUsedAt() { return usedAt; }
    public void setUsedAt(LocalDateTime v) { usedAt = v; }
    public String getNote() { return note; }
    public void setNote(String v) { note = v; }
}