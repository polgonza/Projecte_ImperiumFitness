package com.imperiumfitness.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")          // Aplica CORS a todos tus endpoints de API
                .allowedOrigins(
                        "http://localhost:5500",    // El puerto donde sirves el HTML en desarrollo
                        "http://127.0.0.1:5500",
                        "http://localhost:8080",    // Por si acaso pruebas la web en otro puerto
                        "http://localhost:3000",    // Típico de React/Node, por si acaso
                        "capacitor://localhost",    // Para Android (WebView nativo)
                        "http://localhost"          // Para emuladores Android antiguos
                        // Añade aquí la IP del servidor de producción del frontend si la tiene
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}