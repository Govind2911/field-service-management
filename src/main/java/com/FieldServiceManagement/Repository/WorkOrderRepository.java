package com.FieldServiceManagement.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.FieldServiceManagement.Entity.WorkOrder;
import com.FieldServiceManagement.ENUM.WorkOrderStatus;

public interface WorkOrderRepository
        extends JpaRepository<WorkOrder, Long> {

    List<WorkOrder> findByCustomerId(Long customerId);

    List<WorkOrder> findByAssignedTechnicianId(Long technicianId);

    List<WorkOrder> findByStatus(WorkOrderStatus status);
}