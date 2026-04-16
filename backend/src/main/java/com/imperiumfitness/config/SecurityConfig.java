package com.imperiumfitness.config;

import com.imperiumfitness.security.JwtAuthFilter;
import com.imperiumfitness.security.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, JwtService jwtService) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                // Estáticos (Spring Boot sirve /static automáticamente)
                .requestMatchers(
                        "/", "/index.html",
                        "/login.html", "/profile.html", "/hello.html",
                        "/favicon.ico",
                        "/*.css", "/*.js",
                        "/css/**", "/js/**", "/images/**", "/webjars/**"
                ).permitAll()
                //restringimos acceso a ruta estatica admin
                .requestMatchers("/admin.html").hasRole("ADMIN")
                // Auth público
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/hello/usuaris").permitAll()
                // Resto protegido
                .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthFilter(jwtService), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
