package com.example.secureapi.web.dto;

import java.util.List;

public record ProfileDto(
        String id,
        String username,
        List<String> roles
) {}
