package com.web_app.web_app.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home(
            @RegisteredOAuth2AuthorizedClient("salesforce")
            OAuth2AuthorizedClient authorizedClient) {

        return "Salesforce Login Successful! "
                + "Token available: "
                + (authorizedClient.getAccessToken() != null);
    }
}
