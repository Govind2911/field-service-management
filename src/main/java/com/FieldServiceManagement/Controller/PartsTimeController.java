package com.FieldServiceManagement.Controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.FieldServiceManagement.DTO.*;
import com.FieldServiceManagement.Service.PartsTimeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class PartsTimeController {

    private final PartsTimeService service;

    public PartsTimeController(PartsTimeService service) {
        this.service = service;
    }

    @PostMapping("/parts")
    public ResponseEntity<Map<String, Object>> createPart(
            @Valid @RequestBody CreatePartDTO dto, Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createPart(dto, principal.getName()));
    }

    @GetMapping("/parts")
    public List<Map<String, Object>> listParts() {
        return service.listParts();
    }

    @PatchMapping("/parts/{id}/stock")
    public Map<String, Object> addStock(
            @PathVariable Long id,
            @RequestParam Integer quantity,
            Principal principal) {
        return service.addStock(id, quantity, principal.getName());
    }

    @PostMapping("/work-orders/{id}/parts")
    public ResponseEntity<Map<String, Object>> usePart(
            @PathVariable Long id,
            @Valid @RequestBody UsePartDTO dto,
            Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.usePart(id, dto, principal.getName()));
    }

    @PostMapping("/work-orders/{id}/time-logs")
    public ResponseEntity<Map<String, Object>> logTime(
            @PathVariable Long id,
            @Valid @RequestBody CreateTimeLogDTO dto,
            Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.logTime(id, dto, principal.getName()));
    }

    @GetMapping("/work-orders/{id}/costs")
    public Map<String, Object> summary(
            @PathVariable Long id, Principal principal) {
        return service.summary(id, principal.getName());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(
            IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(Map.of("message", ex.getMessage()));
    }
}