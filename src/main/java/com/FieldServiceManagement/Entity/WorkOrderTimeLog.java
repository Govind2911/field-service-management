package com.FieldServiceManagement.Entity;


import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "work_order_time_logs")
public class WorkOrderTimeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "work_order_id", nullable = false)
    private WorkOrder workOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "technician_id", nullable = false)
    private UserAuth technician;

    @Column(nullable = false)
    private Integer minutes;

    @Column(length = 1000)
    private String note;

    @Column(nullable = false)
    private LocalDateTime loggedAt;

    @PrePersist
    public void onCreate() {
        if (loggedAt == null) loggedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long v) { id = v; }
    public WorkOrder getWorkOrder() { return workOrder; }
    public void setWorkOrder(WorkOrder v) { workOrder = v; }
    public UserAuth getTechnician() { return technician; }
    public void setTechnician(UserAuth v) { technician = v; }
    public Integer getMinutes() { return minutes; }
    public void setMinutes(Integer v) { minutes = v; }
    public String getNote() { return note; }
    public void setNote(String v) { note = v; }
    public LocalDateTime getLoggedAt() { return loggedAt; }
    public void setLoggedAt(LocalDateTime v) { loggedAt = v; }
}