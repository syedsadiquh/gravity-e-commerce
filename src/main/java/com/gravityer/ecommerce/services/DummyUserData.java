package com.gravityer.ecommerce.services;

import com.gravityer.ecommerce.models.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DummyUserData {

    private final UserService userService;

    public DummyUserData(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        userService.users = new ArrayList<>();
        User user1 = new User(1L, "syed", "sy@123", "syed@test.com");
        User user2 = new User(2L, "sadiqu", "sa@123", "sadiqu@test.com");
        User user3 = new User(3L, "hussain", "hu@123", "hussain@test.com");
        userService.users.add(user1);
        userService.users.add(user2);
        userService.users.add(user3);
    }
}
