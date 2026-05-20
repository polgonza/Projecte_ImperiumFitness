package com.imperiumfitness.service;

import com.imperiumfitness.model.entity.Usuari;
import com.imperiumfitness.repository.UsuariRepository;
import com.imperiumfitness.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UsuariRepository usuariRepository;

    public AuthService(PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       UsuariRepository usuariRepository) {
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.usuariRepository = usuariRepository;
    }

    public String login(String email, String rawPassword) {

        Usuari usuari = usuariRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Usuari no trobat"));

        if (!passwordEncoder.matches(rawPassword, usuari.getContrasenya())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Contrasenya incorrecta");
        }

        String role = normalitzaRol(usuari.getRol());

        return jwtService.issueToken(
                String.valueOf(usuari.getId()),
                usuari.getEmail(),
                List.of(role)
        );
    }
    public String registre(String nom, String email, String rawPassword) {

    if (usuariRepository.existsByEmail(email)) {
        throw new ResponseStatusException(
                HttpStatus.CONFLICT, "Aquest email ja està registrat");
    }

    Usuari nouUsuari = new Usuari();
    nouUsuari.setNom(nom);
    nouUsuari.setEmail(email);
    nouUsuari.setContrasenya(passwordEncoder.encode(rawPassword));
    nouUsuari.setRol("USER");
    nouUsuari.setDataRegistre(LocalDateTime.now());

    usuariRepository.save(nouUsuari);

    return jwtService.issueToken(
            String.valueOf(nouUsuari.getId()),
            nouUsuari.getEmail(),
            List.of("ROLE_USER")
    );
}

    private String normalitzaRol(String rolDeBD) {
        if (rolDeBD == null || rolDeBD.isBlank()) return "ROLE_USER";
        String rol = rolDeBD.trim().toUpperCase();
        return rol.startsWith("ROLE_") ? rol : "ROLE_" + rol;
    }
}