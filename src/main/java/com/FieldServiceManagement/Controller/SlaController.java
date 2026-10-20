package com.FieldServiceManagement.Controller;


import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.FieldServiceManagement.DTO.SlaResponseDTO;
import com.FieldServiceManagement.Service.SlaService;

@RestController
@RequestMapping("/api/sla")
public class SlaController {

    private final SlaService slaService;

    public SlaController(SlaService slaService) {
        this.slaService = slaService;
    }

    @GetMapping("/breached")
    public ResponseEntity<List<SlaResponseDTO>> getBreachedOrders(
            Principal principal) {
        return ResponseEntity.ok(
            slaService.getBreachedOrders(principal.getName()));
    }

    @GetMapping("/work-orders/{id}")
    public ResponseEntity<SlaResponseDTO> getSlaDetails(
            @PathVariable Long id,
            Principal principal) {
        return ResponseEntity.ok(
            slaService.getSlaDetails(id, principal.getName()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(
            IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(ex.getMessage()));
    }

    public record ErrorResponse(String message) {}
}