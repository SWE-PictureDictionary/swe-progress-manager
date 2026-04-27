package com.swe.project.progressmanager.client;

import com.swe.project.progressmanager.dto.ClickRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class ProgressAccessClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${progress.access.url:http://progress-access}")
    private String progressAccessUrl;

    public void recordClick(String learnerId, String topicId, String label) {
        String url = progressAccessUrl + "/progress/click";

        ClickRequest request = new ClickRequest(learnerId, topicId, label);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ClickRequest> entity = new HttpEntity<>(request, headers);

        restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Void.class
        );
    }

    public ResponseEntity<String> getProgress(String learnerId) {
        String url = progressAccessUrl + "/progress/" + learnerId;

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                String.class
        );
    }

    public ResponseEntity<String> getProgressForTopic(String learnerId, String topicId) {
        String url = progressAccessUrl + "/progress/" + learnerId + "/topic/" + topicId;

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                String.class
        );
    }

    public ResponseEntity<String> undoLatestClick(String learnerId, String topicId) {
        String url = progressAccessUrl + "/progress/" + learnerId + "/topic/" + topicId + "/latest";

        try {
            return restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    null,
                    String.class
            );
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }
}