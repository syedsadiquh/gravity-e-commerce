package com.gravityer.ecommerce.controller;

import com.gravityer.ecommerce.dto.RegisterDto;
import com.gravityer.ecommerce.models.AppUser;
import com.gravityer.ecommerce.services.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AppAdminController {
    private final AppUserService appUserService;

    @PostMapping("/register")
    public AppUser registerAdmin(@RequestBody RegisterDto registerDto) {
        return appUserService.registerAdmin(registerDto);
    }
}
