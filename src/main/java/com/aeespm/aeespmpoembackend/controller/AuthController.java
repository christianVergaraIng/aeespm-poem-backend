package com.aeespm.aeespmpoembackend.controller;

import com.aeespm.aeespmpoembackend.dto.LoginRequest;
import com.aeespm.aeespmpoembackend.dto.LoginResponse;
import com.aeespm.aeespmpoembackend.security.JwtUtil;
import com.aeespm.aeespmpoembackend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        if (!userService.validateCredentials(request.getUsername(), request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(null, "Credenciales inválidas"));
        }

        String token = jwtUtil.generateToken(request.getUsername());
        return ResponseEntity.ok(new LoginResponse(token, "Login exitoso"));
    }
}
