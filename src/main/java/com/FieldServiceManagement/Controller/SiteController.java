package com.FieldServiceManagement.Controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.FieldServiceManagement.Entity.Site;
import com.FieldServiceManagement.Service.SiteService;

@RestController
@RequestMapping("/api/sites")
public class SiteController {

    private final SiteService siteService;

    public SiteController(SiteService siteService) {
        this.siteService = siteService;
    }

    @PostMapping
    public ResponseEntity<Site> createSite(@RequestBody Site site) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(siteService.createSite(site));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Site> getSite(@PathVariable Long id) {
        return ResponseEntity.ok(siteService.getSite(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Site>> getSiteByCustomer(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(
                siteService.getSiteByCustomer(customerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Site> updateSite(
            @PathVariable Long id,
            @RequestBody Site siteDetails) {
        return ResponseEntity.ok(
                siteService.updateSite(id, siteDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSite(@PathVariable Long id) {
        siteService.deleteSite(id);
        return ResponseEntity.noContent().build();
    }
}
