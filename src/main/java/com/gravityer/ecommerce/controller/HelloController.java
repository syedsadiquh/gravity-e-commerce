package com.gravityer.ecommerce.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Tag(name = "Home Controller", description = "Controller for Hello World endpoint")
public class HelloController {

    @GetMapping("/")
    @ResponseBody
    public String getHello() {
        return "<h1>Hello World<h1>";
    }
}
