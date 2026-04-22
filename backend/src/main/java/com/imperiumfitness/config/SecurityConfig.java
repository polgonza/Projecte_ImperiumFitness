package com.imperiumfitness.config;

import com.imperiumfitness.repository.UsuariRepository;
import com.imperiumfitness.security.JwtAuthFilter;
import com.imperiumfitness.security.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
        .requestMatchers(
                "/", "/index.html", "/login.html",
                "/*.css", "/*.js",
                "/css/**", "/js/**", "/images/**"
        ).permitAll()

        // Endpoints públics
        .requestMatchers("/api/auth/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/noticies/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/classes/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/productes/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/tarifes/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/contactes").permitAll()

        // Endpoints de perfil — accessibles per USER i ADMIN
        .requestMatchers(HttpMethod.GET, "/api/usuaris/perfil/**").authenticated()
        .requestMatchers(HttpMethod.GET, "/api/reserves/usuari/**").authenticated()
        .requestMatchers(HttpMethod.GET, "/api/vendes/usuari/**").authenticated()
        .requestMatchers(HttpMethod.POST, "/api/reserves").authenticated()
        .requestMatchers(HttpMethod.POST, "/api/vendes").authenticated()
        .requestMatchers(HttpMethod.DELETE, "/api/reserves/**").authenticated()

        // Endpoints ADMIN
        .requestMatchers("/api/usuaris/**").hasRole("ADMIN")
        .requestMatchers("/api/estadistiques/**").hasRole("ADMIN")

        .requestMatchers("/api/**").authenticated()
        .anyRequest().authenticated()
)
            .addFilterBefore(
                    new JwtAuthFilter(jwtService),
                    UsernamePasswordAuthenticationFilter.class)
            .build();
}
}