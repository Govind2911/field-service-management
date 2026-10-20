package com.FieldServiceManagement.Repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.FieldServiceManagement.Entity.WorkOrderPartUsage;

public interface WorkOrderPartUsageRepository
        extends JpaRepository<WorkOrderPartUsage, Long> {

    List<WorkOrderPartUsage> findByWorkOrderId(Long workOrderId);
}