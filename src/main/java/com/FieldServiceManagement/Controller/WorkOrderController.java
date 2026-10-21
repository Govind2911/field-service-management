package com.FieldServiceManagement.Controller;


import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.FieldServiceManagement.DTO.AssignTechnicianDTO;
import com.FieldServiceManagement.DTO.CreateWorkOrderDTO;
import com.FieldServiceManagement.DTO.WorkOrderHistoryResponseDTO;
import com.FieldServiceManagement.DTO.WorkOrderResponseDTO;
import com.FieldServiceManagement.DTO.WorkOrderStatusUpdateDTO;
import com.FieldServiceManagement.Service.WorkOrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/work-orders")
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    public WorkOrderController(WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    @PostMapping("/create")
    public ResponseEntity<WorkOrderResponseDTO> create(
            @Valid @RequestBody CreateWorkOrderDTO dto,
            Principal principal) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workOrderService.create(dto, principal.getName()));
    }

    @GetMapping("/All orders")
    public ResponseEntity<List<WorkOrderResponseDTO>> getAll() {
        return ResponseEntity.ok(workOrderService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkOrderResponseDTO> getById(
            @PathVariable Long id,
            Principal principal) {

        return ResponseEntity.ok(
                workOrderService.getById(id, principal.getName()));
    }
    @PatchMapping("/{id}/assign")
    public ResponseEntity<WorkOrderResponseDTO> assign(
            @PathVariable Long id,
            @Valid @RequestBody AssignTechnicianDTO dto,
            Principal principal) {

        return ResponseEntity.ok(
                workOrderService.assign(id, dto, principal.getName()));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<WorkOrderResponseDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody WorkOrderStatusUpdateDTO dto,
            Principal principal) {

        return ResponseEntity.ok(
                workOrderService.updateStatus(
                        id, dto, principal.getName()));
    }
    
    @GetMapping("/my-orders")
    public ResponseEntity<List<WorkOrderResponseDTO>> getMyOrders(
            Principal principal) {

        return ResponseEntity.ok(
                workOrderService.getMyOrders(principal.getName()));
    }
    
    @GetMapping("/technician/assigned")
    public ResponseEntity<List<WorkOrderResponseDTO>> getAssignedOrders(
            Principal principal) {

        return ResponseEntity.ok(
                workOrderService.getAssignedOrders(principal.getName()));
    }

    @GetMapping("/my-orders/{id}")
    public ResponseEntity<WorkOrderResponseDTO> getMyOrderById(
            @PathVariable Long id,
            Principal principal) {

        return ResponseEntity.ok(
                workOrderService.getMyOrderById(id, principal.getName()));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<WorkOrderHistoryResponseDTO>> getHistory(
            @PathVariable Long id) {

        return ResponseEntity.ok(workOrderService.getHistory(id));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(
            IllegalArgumentException ex) {

        return ResponseEntity.badRequest()
                .body(new ErrorResponse(ex.getMessage()));
    }

    public record ErrorResponse(String message) {}
}