package com.web_app.web_app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

@Service
public class SalesforceService {

    private final RestClient restClient;

    @Value("${salesforce.instance-url}")
    private String instanceUrl;

    @Value("${salesforce.api-version}")
    private String apiVersion;

    private  static final Set<String> ALLOWED_OBJECTS = Set.of(
            "Account","Opportunity","Lead","Contact","Case"
    );

    public SalesforceService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String getRecords(
            OAuth2AuthorizedClient authorizedClient,
            String objectName,
            int offset,
            int limit
    ) {

        validateObject(objectName);

        String accessToken = authorizedClient.getAccessToken().getTokenValue();

        limit = Math.min(limit, 20);
        offset = Math.max(offset, 0);

        String soql = buildSoql(objectName, offset, limit);

        String url = instanceUrl
                + "/services/data/"
                + apiVersion
                + "/query";

        URI uri = URI.create(
                url + "?q=" +
                        URLEncoder.encode(
                                soql,
                                StandardCharsets.UTF_8
                        )
        );

        return restClient.get()
                .uri(uri)
                .header(
                        "Authorization",
                        "Bearer " + accessToken
                )
                .retrieve()
                .body(String.class);
    }

    private String buildSoql(
            String objectName,
            int offset,
            int limit
    ) {
        String field = objectName.equals("Case")
                ? "CaseNumber"
                : "Name";

        return "SELECT Id," + field +
                " FROM " + objectName +
                " ORDER BY " + field +
                " LIMIT " + limit +
                " OFFSET " + offset;
    }

    public String createRecord(
            OAuth2AuthorizedClient authorizedClient,
            String objectName,
            Map<String, Object> data) {

        validateObject(objectName);

        String accessToken =
                authorizedClient.getAccessToken().getTokenValue();

        String url =
                instanceUrl
                        + "/services/data/"
                        + apiVersion
                        + "/sobjects/"
                        + objectName;

        return restClient.post()
                .uri(url)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(data)
                .retrieve()
                .body(String.class);
    }

    public void updateRecord(
            OAuth2AuthorizedClient authorizedClient,
            String objectName,
            String id,
            Map<String, Object> data
    ) {

        validateObject(objectName);

        String accessToken =
                authorizedClient.getAccessToken().getTokenValue();

        String url =
                instanceUrl +
                        "/services/data/" +
                        apiVersion +
                        "/sobjects/" +
                        objectName +
                        "/" +
                        id;

        restClient.patch()
                .uri(url)
                .header(
                        "Authorization",
                        "Bearer " + accessToken
                )
                .body(data)
                .retrieve()
                .toBodilessEntity();
    }
    public void deleteRecord(
            OAuth2AuthorizedClient authorizedClient,
            String objectName,
            String id
    ) {

        validateObject(objectName);

        String accessToken =
                authorizedClient.getAccessToken().getTokenValue();

        String url =
                instanceUrl +
                        "/services/data/" +
                        apiVersion +
                        "/sobjects/" +
                        objectName +
                        "/" +
                        id;

        restClient.delete()
                .uri(url)
                .header(
                        "Authorization",
                        "Bearer " + accessToken
                )
                .retrieve()
                .toBodilessEntity();
    }


    private void validateObject(String objectName) {

        if (!ALLOWED_OBJECTS.contains(objectName)) {
            throw new IllegalArgumentException(
                    "Unsupported Salesforce object: " + objectName
            );
        }
    }
}
