package com.imperiumfitness.service;

import com.imperiumfitness.DAO.usuarisDAO;
import com.imperiumfitness.domain.Usuari;
import com.imperiumfitness.security.JwtService;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    private DataSource dataSource;

    public AuthService(PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String login(String username, String rawPassword) {
        // OJO:
        // de momento "username" realmente va a ser el email,
        // porque LoginRequest todavía se llama username.
        Usuari usuari = usuarisDAO.findByEmail(dataSource, username);

        if (usuari == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado");
        }

        if (!passwordMatches(rawPassword, usuari.getContrasenya())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Contraseña incorrecta");
        }

        String role = normalizeRole(usuari.getRol());

        return jwtService.issueToken(
                String.valueOf(usuari.getId()),
                usuari.getEmail(),
                List.of(role)
        );
    }

    private String normalizeRole(String roleFromDb) {
        if (roleFromDb == null || roleFromDb.isBlank()) {
            return "ROLE_USER";
        }

        String role = roleFromDb.trim().toUpperCase();

        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        return role;
    }

    private boolean passwordMatches(String rawPassword, String storedPassword) {
        if (storedPassword == null) {
            return false;
        }

        // Si la contraseña está cifrada con Spring ({bcrypt}, {noop}, etc.)
        if (storedPassword.startsWith("{")) {
            return passwordEncoder.matches(rawPassword, storedPassword);
        }

        // Compatibilidad temporal si en tu BD hay contraseñas en texto plano
        return rawPassword.equals(storedPassword);
    }
}