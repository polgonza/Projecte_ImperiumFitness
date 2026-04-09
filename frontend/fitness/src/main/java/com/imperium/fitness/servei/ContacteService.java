package com.imperium.fitness.servei;

import com.imperium.fitness.entitat.Contacte;
import com.imperium.fitness.repositori.ContacteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ContacteService {
    
    @Autowired
    private ContacteRepository contacteRepository;

    public Contacte guardar(Contacte contacte) {
        return contacteRepository.save(contacte);
    }
    
    public List<Contacte> obtenirTots() {
        return contacteRepository.findAllByOrderByDataEnviamentDesc();
    }
    
    public Contacte obtenirPerId(Long id) {
        return contacteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Mensaje no encontrado con ID: " + id));
    }
    
    public void eliminar(Long id) {
        if (!contacteRepository.existsById(id)) {
            throw new RuntimeException("Mensaje no encontrado con ID: " + id);
        }
        contacteRepository.deleteById(id);
    }
}