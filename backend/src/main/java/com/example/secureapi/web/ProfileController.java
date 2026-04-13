package com.example.secureapi.web;

import com.example.secureapi.service.ProfileService;
import com.example.secureapi.web.dto.ProfileDto;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }

    @GetMapping("/me")
    public ProfileDto me(Authentication auth) {
        //return service.profile(auth.getName());
        return service.getMyProfile(auth.getName());
    }
}
