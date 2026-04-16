package com.imperiumfitness.service;

import com.imperiumfitness.domain.UserAccount;
import com.imperiumfitness.repository.InMemoryUserRepo;
import com.imperiumfitness.web.dto.ProfileDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProfileService {

    private final InMemoryUserRepo repo;

    public ProfileService(InMemoryUserRepo repo) {
        this.repo = repo;
    }

    public String profile(String username) {
        return repo.findByUsername(username).orElseThrow().getUsername();
    }
    
    public ProfileDto getMyProfile(String username) {
        UserAccount user = repo.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return new ProfileDto(
                user.getId(),
                user.getUsername(),
                user.getRoles().stream().toList() 
        );
    }
}
