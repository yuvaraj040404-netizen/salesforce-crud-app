package com.web_app.web_app.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/api/auth/status")
    public boolean status(Authentication authentication) {
        return authentication instanceof OAuth2AuthenticationToken;
    }
}