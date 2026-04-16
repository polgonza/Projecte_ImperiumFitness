package com.imperiumfitness.config;

import com.imperiumfitness.repository.UsuariRepository;
import com.imperiumfitness.security.JwtAuthFilter;
import com.imperiumfitness.security.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ── Carreguem l'usuari des de MariaDB per email ──────────────────────────
    // Spring Security usa aquest bean per saber qui és l'usuari autenticat.
    // El "username" en el nostre cas és l'email.
    @Bean
    public UserDetailsService userDetailsService(UsuariRepository usuariRepository) {
        return email -> {
            var usuari = usuariRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            "Usuari no trobat: " + email));

            // Normalitzem el rol per Spring Security
            String rol = usuari.getRol() != null ? usuari.getRol().toUpperCase() : "USER";
            if (!rol.startsWith("ROLE_")) rol = "ROLE_" + rol;

            return User.builder()
                    .username(usuari.getEmail())
                    .password(usuari.getContrasenya())
                    .authorities(List.of(new SimpleGrantedAuthority(rol)))
                    .build();
        };
    }

    // ── AuthenticationManager: necessari si afegim registre més endavant ────
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, JwtService jwtService) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Recursos estàtics
                      /*   .requestMatchers(
                                "/", "/index.html", "/login.html",
                                // "/favicon.ico",
                                "/*.css", "/*.js",
                                "/css/**", "/js/**", "/images/**"
                        ).permitAll()
                        // Endpoints públics
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("GET", "/api/noticies/**").permitAll()
                        .requestMatchers("POST", "/api/contactes").permitAll()
                        // Endpoints protegits
                        .requestMatchers("/api/usuaris/**").hasRole("ADMIN")
                        .requestMatchers("/api/estadistiques/**").hasRole("ADMIN")
                        // Resta: autenticats
                        .anyRequest().authenticated()
                        */
                        .anyRequest().permitAll()
                )
                .addFilterBefore(
                        new JwtAuthFilter(jwtService),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}