package com.imperium.fitness.servei;

import com.imperium.fitness.entitat.Usuario;
import com.imperium.fitness.repositori.UsuariRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuariService {
    
    @Autowired
    private UsuariRepository usuariRepository;
    public Usuario registrar(Usuario usuario) {
        if (usuariRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email " + usuario.getEmail() + " ya está registrado");
        }
        
        usuario.setDataRegistre(LocalDateTime.now());
        usuario.setRol("USER");
        
        return usuariRepository.save(usuario);
    }
    
    public Usuario login(String email, String contrasenya) {
        Optional<Usuario> usuarioOpt = usuariRepository.findByEmail(email);
        
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Email no encontrado");
        }
        
        Usuario usuario = usuarioOpt.get();
        
        if (!usuario.getContrasenya().equals(contrasenya)) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        
        return usuario;
    }

    public List<Usuario> obtenirTots() {
        return usuariRepository.findAll();
    }
    
    public Usuario obtenirPerId(Long id) {
        return usuariRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }
}