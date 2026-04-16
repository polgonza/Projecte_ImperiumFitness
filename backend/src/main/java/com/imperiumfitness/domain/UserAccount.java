package com.imperiumfitness.domain;

import java.util.Set;

public class UserAccount {

    private final String id;
    private final String username;
    private final String password;
    private final Set<String> roles;

    public UserAccount(String id, String username, String password, Set<String> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() {   
        return password;
    }
    
    public Set<String> getRoles() { return roles; }
}

