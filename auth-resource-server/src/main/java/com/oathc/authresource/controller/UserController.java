package com.oathc.authresource.controller;

import com.oathc.authresource.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/get_resource")
    public String getResource() {
        return "OK";
    }

    @GetMapping("/user")
    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        User user = new User();
        user.setName(name);
        return user;
    }

    @GetMapping("/me")
    public User getMe(Authentication authentication) {
        User user = new User();
        user.setName(authentication.getName());
        return user;
    }
}
