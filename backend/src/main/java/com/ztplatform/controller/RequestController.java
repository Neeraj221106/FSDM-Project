package com.ztplatform.controller;

import com.ztplatform.dto.DecideRequestDTO;
import com.ztplatform.dto.RequestDTO;
import com.ztplatform.model.User;
import com.ztplatform.service.AuthService;
import com.ztplatform.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<RequestDTO> createRequest(
            @RequestBody RequestDTO requestDTO,
            Authentication authentication
    ) {
        User studentUser = authService.findByEmail(authentication.getName());

        return ResponseEntity.ok(
                requestService.createRequest(requestDTO, studentUser)
        );
    }

    @GetMapping
    public ResponseEntity<List<RequestDTO>> getAllRequests() {
        return ResponseEntity.ok(
                requestService.getAllRequests()
        );
    }

    @GetMapping("/my")
    public ResponseEntity<List<RequestDTO>> getMyRequests(
            Authentication authentication
    ) {
        User studentUser = authService.findByEmail(authentication.getName());

        return ResponseEntity.ok(
                requestService.getMyRequests(studentUser)
        );
    }

    @PostMapping("/{id}/decide")
    public ResponseEntity<RequestDTO> decideRequest(
            @PathVariable Long id,
            @RequestBody DecideRequestDTO decideRequestDTO,
            Authentication authentication
    ) {
        User actor = authService.findByEmail(authentication.getName());
        String action = decideRequestDTO.getAction();

        if (action != null && action.equalsIgnoreCase("approve")) {
            return ResponseEntity.ok(
                    requestService.approveRequest(id, actor)
            );
        }

        if (action != null && action.equalsIgnoreCase("reject")) {
            return ResponseEntity.ok(
                    requestService.rejectRequest(
                            id,
                            actor,
                            decideRequestDTO.getRejectionReason()
                    )
            );
        }

        throw new RuntimeException("Invalid decision action");
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestDTO> getRequestById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                requestService.getRequestById(id)
        );
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<RequestDTO>> getRequestsByStudent(
            @PathVariable Long studentId
    ) {
        return ResponseEntity.ok(
                requestService.getRequestsByStudent(studentId)
        );
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<RequestDTO> approveRequest(
            @PathVariable Long id,
            @RequestParam String adminEmail
    ) {
        User admin = authService.findByEmail(adminEmail);

        return ResponseEntity.ok(
                requestService.approveRequest(id, admin)
        );
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<RequestDTO> rejectRequest(
            @PathVariable Long id,
            @RequestParam String adminEmail,
            @RequestParam String reason
    ) {
        User admin = authService.findByEmail(adminEmail);

        return ResponseEntity.ok(
                requestService.rejectRequest(id, admin, reason)
        );
    }
}