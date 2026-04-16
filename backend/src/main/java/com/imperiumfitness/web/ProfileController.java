package com.imperiumfitness.web;

import com.imperiumfitness.service.ProfileService;
import com.imperiumfitness.web.dto.ProfileDto;
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
