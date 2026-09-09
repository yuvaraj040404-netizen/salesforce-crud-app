package com.web_app.web_app.controller;

import com.web_app.web_app.service.SalesforceService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/salesforce")
public class SalesforceController {

    private final SalesforceService salesforceService;

    public SalesforceController(SalesforceService salesforceService) {
        this.salesforceService = salesforceService;
    }

    @GetMapping("/{objectName}")
    public String getRecords(
            @PathVariable String objectName,

            @RequestParam(defaultValue = "0")
            int offset,

            @RequestParam(defaultValue = "20")
            int limit,

            @RegisteredOAuth2AuthorizedClient("salesforce")
            OAuth2AuthorizedClient authorizedClient
    ) {

        return salesforceService.getRecords(
                authorizedClient,
                objectName,
                offset,
                limit
        );
    }
    @PostMapping("/{objectName}")
    public String createRecord(
            @PathVariable String objectName,

            @RequestBody Map<String, Object> data,

            @RegisteredOAuth2AuthorizedClient("salesforce")
            OAuth2AuthorizedClient authorizedClient
    ) {

        return salesforceService.createRecord(
                authorizedClient,
                objectName,
                data
        );
    }
    @PatchMapping("/{objectName}/{id}")
    public void updateRecord(
            @PathVariable String objectName,
            @PathVariable String id,

            @RequestBody Map<String, Object> data,

            @RegisteredOAuth2AuthorizedClient("salesforce")
            OAuth2AuthorizedClient authorizedClient
    ) {

        salesforceService.updateRecord(
                authorizedClient,
                objectName,
                id,
                data
        );
    }
    @DeleteMapping("/{objectName}/{id}")
    public void deleteRecord(
            @PathVariable String objectName,
            @PathVariable String id,

            @RegisteredOAuth2AuthorizedClient("salesforce")
            OAuth2AuthorizedClient authorizedClient
    ) {

        salesforceService.deleteRecord(
                authorizedClient,
                objectName,
                id
        );
    }
}
