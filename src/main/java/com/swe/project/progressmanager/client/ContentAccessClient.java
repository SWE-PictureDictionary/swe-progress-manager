package com.swe.project.progressmanager.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ContentAccessClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${content.access.url:http://content-access}")
    private String contentAccessUrl;

    public String getTopicRaw(String topicId) {
        String url = contentAccessUrl + "/topics/" + topicId;

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                String.class
        );

        return response.getBody();
    }
}