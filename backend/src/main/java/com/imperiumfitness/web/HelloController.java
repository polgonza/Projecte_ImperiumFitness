package com.imperiumfitness.web;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imperiumfitness.DAO.usuarisDAO;
import com.imperiumfitness.domain.Usuari;

@RestController
@RequestMapping("/api/hello")
public class HelloController {

    @Autowired
private DataSource dataSource;

@GetMapping("/usuaris")
public List<Usuari> usuaris() {
    return usuarisDAO.getUsuaris(dataSource);
}

    @GetMapping("/user")
    //@PreAuthorize("hasRole('USER')")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String helloUser() {
        return "Hello, User";
    }
    
    @GetMapping("/gestor")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String helloGestor() {
        return "Hello, gestor";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String helloAdmin() {
        return "Hello, Admin";
    }

    
}
