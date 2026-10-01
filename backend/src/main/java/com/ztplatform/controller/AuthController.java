package com.ztplatform.controller;

import com.ztplatform.dto.AuthDTO;
import com.ztplatform.dto.LoginResponseDTO;
import com.ztplatform.model.User;
import com.ztplatform.service.AuditService;
import com.ztplatform.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuditService auditService;

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody AuthDTO authDTO,
            HttpServletRequest request) {

        LoginResponseDTO response = authService.login(authDTO);

        try {
            User user = authService.findByEmail(response.getEmail());
            auditService.log(
                    user,
                    "LOGIN",
                    "USER",
                    user.getId(),
                    request.getRemoteAddr(),
                    user.getRole().name() + " logged in"
            );
        } catch (Exception ignored) {
            // Login must succeed even if audit persistence fails
        }

        return ResponseEntity.ok(response);
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {

        User createdUser = authService.register(user);

        // Password response mein kabhi return nahi karna
        createdUser.setPassword(null);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }
}