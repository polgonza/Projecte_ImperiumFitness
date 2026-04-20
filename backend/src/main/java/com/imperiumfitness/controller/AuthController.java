package com.imperiumfitness.controller;

import com.imperiumfitness.dto.LoginRequest;
import com.imperiumfitness.dto.LoginResponse;
import com.imperiumfitness.dto.RegistreRequest;
import com.imperiumfitness.service.AuthService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder; // afegim això

    public AuthController(AuthService authService, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        String token = authService.login(req.username(), req.password());
        ResponseCookie adminCookie = ResponseCookie.from("ADMIN_AUTH", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(Duration.ofSeconds(1800))
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, adminCookie.toString())
                .body(new LoginResponse(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = ResponseCookie.from("ADMIN_AUTH", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
    @PostMapping("/registre")
public ResponseEntity<LoginResponse> registre(@RequestBody RegistreRequest req) {

    // Cridem el service que valida, hasheja i guarda
    String token = authService.registre(
            req.nom(),
            req.email(),
            req.password()
    );

    // Retornem el token igual que al login
    return ResponseEntity.status(201).body(new LoginResponse(token));
}
    // ── TEMPORAL: genera hash BCrypt — ELIMINA DESPRÉS DE LES PROVES ────────
    @GetMapping("/hash")
    public String generaHash(@RequestParam String password) {
        return passwordEncoder.encode(password);
    }
}