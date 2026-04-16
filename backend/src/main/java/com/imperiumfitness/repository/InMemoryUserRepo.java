package com.imperiumfitness.repository;

import com.imperiumfitness.domain.UserAccount;
import org.springframework.stereotype.Repository;

import java.util.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Repository
public class InMemoryUserRepo {

    private final Map<String, UserAccount> users = new HashMap<>();

    public InMemoryUserRepo(PasswordEncoder encoder) {
        users.put("alice",
            new UserAccount("1", "alice", encoder.encode("alice123"), Set.of("ROLE_USER")));
            users.put("admin",
            new UserAccount("2", "admin", encoder.encode("admin123"), Set.of("ROLE_ADMIN")));
        
        //users.put("admin",new UserAccount("2", "admin", encoder.encode("admin123"), Set.of("ROLE_ADMIN")));
        
    }

    public Optional<UserAccount> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }
}
