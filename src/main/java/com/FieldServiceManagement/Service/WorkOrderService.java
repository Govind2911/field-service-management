package com.FieldServiceManagement.Service;


import java.util.List;

import com.FieldServiceManagement.DTO.AssignTechnicianDTO;
import com.FieldServiceManagement.DTO.CreateWorkOrderDTO;
import com.FieldServiceManagement.DTO.WorkOrderHistoryResponseDTO;
import com.FieldServiceManagement.DTO.WorkOrderResponseDTO;
import com.FieldServiceManagement.DTO.WorkOrderStatusUpdateDTO;

public interface WorkOrderService {

    WorkOrderResponseDTO create(
            CreateWorkOrderDTO dto, String email);

    List<WorkOrderResponseDTO> getAll();

    WorkOrderResponseDTO getById(Long id, String email);

    WorkOrderResponseDTO assign(
            Long workOrderId,
            AssignTechnicianDTO dto,
            String email);

    WorkOrderResponseDTO updateStatus(
            Long workOrderId,
            WorkOrderStatusUpdateDTO dto,
            String email);

    List<WorkOrderHistoryResponseDTO> getHistory(Long workOrderId);
    
    List<WorkOrderResponseDTO> getMyOrders(String email);

    WorkOrderResponseDTO getMyOrderById(Long id, String email);
}
