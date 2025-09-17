package com.gravityer.ecommerce.controller;

import com.gravityer.ecommerce.dto.RegisterDto;
import com.gravityer.ecommerce.models.AppUser;
import com.gravityer.ecommerce.services.AppUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class AppUsersController {

    private final AppUserService appUserService;

    @PostMapping("/register")
    public AppUser registerUser(@RequestBody RegisterDto registerDto) {
        return appUserService.registerUser(registerDto);
    }

    @PostMapping("/login")
    public String login(@RequestBody AppUser appUser) {
        return appUserService.login(appUser);
    }
}
