package com.gravityer.ecommerce.controller;

import com.gravityer.ecommerce.dto.UserDto;
import com.gravityer.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getUsers")
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

}
