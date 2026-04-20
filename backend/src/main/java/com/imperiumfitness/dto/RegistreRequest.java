package com.imperiumfitness.dto;

public record RegistreRequest (
    String nom,
    String email,
    String password
) {}
