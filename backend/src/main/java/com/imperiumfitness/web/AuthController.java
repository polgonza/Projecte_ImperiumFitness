package com.imperiumfitness.web;

import com.imperiumfitness.DAO.usuarisDAO;
import com.imperiumfitness.service.AuthService;
import com.imperiumfitness.web.dto.LoginRequest;
import com.imperiumfitness.web.dto.LoginResponse;
import java.time.Duration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {

        String token = authService.login(req.username(),req.password());
        

        ResponseCookie adminCookie = ResponseCookie.from("ADMIN_AUTH", token)
                .httpOnly(true)
                .secure(false) // true en HTTPS
                .path("/") // o "/admin.html" si quieres limitar más (ver nota abajo)
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
}
