package com.swe.project.progressmanager.client;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.swe.project.progressmanager.dto.*;

@Component
public class CompletionEngineClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${completion.engine.url:http://completion-engine}")
    private String completionEngineUrl;

     public CompletionResponse checkCompletion(Set<String> allLabels, Set<String> clickedLabels) {

        String url = completionEngineUrl + "/completion/check";

        CompletionRequest request =
                new CompletionRequest(allLabels, clickedLabels);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CompletionRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<CompletionResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                CompletionResponse.class
        );

        return response.getBody();
    }

}