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

    // ── Dependències injectades per Spring ───────────────────────────────────
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UsuariRepository usuariRepository; // substitueix l'antic DAO

    public AuthService(PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       UsuariRepository usuariRepository) {
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.usuariRepository = usuariRepository;
    }

    // ── Login: retorna un token JWT si les credencials son correctes ─────────
    public String login(String email, String rawPassword) {

        // Busquem l'usuari per email a la BD via JPA
        Usuari usuari = usuariRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Usuari no trobat"));

        // Comprovem la contrasenya contra el hash BCrypt emmagatzemat
        if (!passwordEncoder.matches(rawPassword, usuari.getContrasenya())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Contrasenya incorrecta");
        }

        // Normalitzem el rol per Spring Security (ha de tenir prefix ROLE_)
        String role = normalitzaRol(usuari.getRol());

        // Generem i retornem el token JWT
        return jwtService.issueToken(
                String.valueOf(usuari.getId()),
                usuari.getEmail(),
                List.of(role)
        );
    }
    public String registre(String nom, String email, String rawPassword) {

    // Comprovem que l'email no estigui ja registrat
    if (usuariRepository.existsByEmail(email)) {
        throw new ResponseStatusException(
                HttpStatus.CONFLICT, "Aquest email ja està registrat");
    }

    // Creem el nou usuari
    Usuari nouUsuari = new Usuari();
    nouUsuari.setNom(nom);
    nouUsuari.setEmail(email);
    // Hashegem la contrasenya amb BCrypt SEMPRE abans de guardar
    nouUsuari.setContrasenya(passwordEncoder.encode(rawPassword));
    nouUsuari.setRol("USER"); // per defecte sempre USER, mai ADMIN
    nouUsuari.setDataRegistre(LocalDateTime.now());

    // Guardem a la BD
    usuariRepository.save(nouUsuari);

    // Retornem un token JWT per deixar l'usuari logat directament
    return jwtService.issueToken(
            String.valueOf(nouUsuari.getId()),
            nouUsuari.getEmail(),
            List.of("ROLE_USER")
    );
}

    // ── Normalitza el rol: "USER" → "ROLE_USER", "ADMIN" → "ROLE_ADMIN" ─────
    private String normalitzaRol(String rolDeBD) {
        if (rolDeBD == null || rolDeBD.isBlank()) return "ROLE_USER";
        String rol = rolDeBD.trim().toUpperCase();
        return rol.startsWith("ROLE_") ? rol : "ROLE_" + rol;
    }
}