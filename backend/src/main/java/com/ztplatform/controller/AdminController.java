package com.ztplatform.controller;

import com.ztplatform.dto.AdminStatsDTO;
import com.ztplatform.dto.AuditLogDTO;
import com.ztplatform.dto.RequestDTO;
import com.ztplatform.service.AdminService;
import com.ztplatform.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final RequestService requestService;
    private final AdminService adminService;

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsDTO> getStats() {
        return ResponseEntity.ok(adminService.getStats());
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<List<AuditLogDTO>> getAuditLogs(
            @RequestParam(defaultValue = "20") int limit
    ) {
        return ResponseEntity.ok(adminService.getAuditLogs(limit));
    }

    @GetMapping("/requests")
    public ResponseEntity<List<RequestDTO>> getAllRequests() {
        return ResponseEntity.ok(
                requestService.getAllRequests()
        );
    }

    @GetMapping("/requests/{id}")
    public ResponseEntity<RequestDTO> getRequest(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                requestService.getRequestById(id)
        );
    }
}