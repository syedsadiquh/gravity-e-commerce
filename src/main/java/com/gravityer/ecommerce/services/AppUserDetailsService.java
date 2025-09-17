package com.gravityer.ecommerce.services;

import com.gravityer.ecommerce.dto.RegisterDto;
import com.gravityer.ecommerce.models.AppUser;
import com.gravityer.ecommerce.models.AppUserPrincipal;
import com.gravityer.ecommerce.repositories.jpa.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user =  appUserRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User with username: " + username + " not found")
        );
        return new AppUserPrincipal(user);
    }
}
