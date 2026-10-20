package com.FieldServiceManagement.Repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.FieldServiceManagement.Entity.WorkOrderTimeLog;

public interface WorkOrderTimeLogRepository
        extends JpaRepository<WorkOrderTimeLog, Long> {

    List<WorkOrderTimeLog> findByWorkOrderId(Long workOrderId);
}