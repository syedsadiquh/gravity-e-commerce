package com.gravityer.ecommerce.services;

import com.gravityer.ecommerce.dto.RegisterDto;
import com.gravityer.ecommerce.models.AppUser;
import com.gravityer.ecommerce.models.AppUserPrincipal;
import com.gravityer.ecommerce.repositories.jpa.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AppUserRepository appUserRepository;
    private final JwtService jwtService;

    public AppUser registerUser(RegisterDto registerDto) {
        AppUser appUser = AppUser.builder()
                .username(registerDto.getUsername())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .roles(Set.of("USER"))
                .build();

        return appUserRepository.save(appUser);
    }

    public AppUser registerAdmin(RegisterDto registerDto) {
        AppUser appUser = AppUser.builder()
                .username(registerDto.getUsername())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .roles(Set.of("USER", "ADMIN"))
                .build();
        return appUserRepository.save(appUser);
    }

    public String login(AppUser appUser) {
        log.info("Entered Verify Login");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(appUser.getUsername(), appUser.getPassword()));
        if (authentication.isAuthenticated()) {
            return "TOKEN : " + jwtService.generateToken(((AppUserPrincipal) authentication.getPrincipal()));
        }
        return "Authentication failed";

    }
}
