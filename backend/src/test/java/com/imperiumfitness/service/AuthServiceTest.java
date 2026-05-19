package com.imperiumfitness.service;

import com.imperiumfitness.model.entity.Usuari;
import com.imperiumfitness.repository.UsuariRepository;
import com.imperiumfitness.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UsuariRepository usuariRepo;
    @Mock private PasswordEncoder  passwordEncoder;
    @Mock private JwtService       jwtService;

    @InjectMocks
    private AuthService authService;

    private Usuari usuariExistent;

    @BeforeEach
    void setUp() {
        usuariExistent = new Usuari();
        usuariExistent.setId(1L);
        usuariExistent.setNom("Test");
        usuariExistent.setEmail("test@imperium.com");
        usuariExistent.setContrasenya("$2a$10$hashBcrypt");
        usuariExistent.setRol("USER");
    }

    // Test 1: Registre amb email duplicat ha de llancar excepcio 409
    @Test
    void registre_emailDuplicat_llaçaConflict() {
        // AuthService usa existsByEmail, no findByEmail
        when(usuariRepo.existsByEmail("test@imperium.com")).thenReturn(true);

        ResponseStatusException ex = assertThrows(
            ResponseStatusException.class,
            () -> authService.registre("Test", "test@imperium.com", "password123")
        );

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        verify(usuariRepo, never()).save(any());
    }

    // Test 2: Registre nou usuari guarda contrasenya encriptada
    @Test
    void registre_nouUsuari_guardaAmbHash() {
        when(usuariRepo.existsByEmail("nou@imperium.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("$2a$10$hashEncriptat");
        when(usuariRepo.save(any(Usuari.class))).thenAnswer(inv -> {
            Usuari u = inv.getArgument(0);
            u.setId(2L);
            return u;
        });
        // lenient perque issueToken es crida internament pero no es l'objecte del test
        lenient().when(jwtService.issueToken(anyString(), anyString(), anyList()))
            .thenReturn("token.jwt.mock");

        assertDoesNotThrow(
            () -> authService.registre("Nou", "nou@imperium.com", "password123")
        );

        verify(passwordEncoder).encode("password123");
        verify(usuariRepo).save(argThat(u ->
            "$2a$10$hashEncriptat".equals(u.getContrasenya())
        ));
    }

    // Test 3: Login amb contrasenya incorrecta ha de llancar 401
    @Test
    void login_contrasenyaIncorrecta_llaçaUnauthorized() {
        when(usuariRepo.findByEmail("test@imperium.com"))
            .thenReturn(Optional.of(usuariExistent));
        when(passwordEncoder.matches("incorrecta", "$2a$10$hashBcrypt"))
            .thenReturn(false);

        ResponseStatusException ex = assertThrows(
            ResponseStatusException.class,
            () -> authService.login("test@imperium.com", "incorrecta")
        );

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
    }
}