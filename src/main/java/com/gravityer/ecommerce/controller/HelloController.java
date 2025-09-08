package com.gravityer.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @GetMapping("/")
    @ResponseBody
    public String getHello() {
        return "<h1>Hello World<h1>";
    }
}
