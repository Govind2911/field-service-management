package com.FieldServiceManagement.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FieldServiceManagement.Entity.WorkOrderStatusHistory;

public interface WorkOrderStatusHistoryRepository
        extends JpaRepository<WorkOrderStatusHistory, Long> {

    List<WorkOrderStatusHistory>
        findByWorkOrderIdOrderByChangedAtDesc(Long workOrderId);
}