package com.gravityer.ecommerce.controller;

import com.gravityer.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getUsers")
    @ResponseBody
    public String getUsers() {
        var userList = userService.getUsers();
        StringBuilder s = new StringBuilder();
        for (var user : userList) {
            s.append(user.toString());
        }
        return s.toString();
    }

}
