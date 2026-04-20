package com.swe.project.progressmanager.service;

import com.swe.project.progressmanager.client.CompletionEngineClient;
import com.swe.project.progressmanager.client.ContentAccessClient;
import com.swe.project.progressmanager.client.ProgressAccessClient;
import com.swe.project.progressmanager.dto.CompletionResponse;

import java.util.Set;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProgressService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final ContentAccessClient contentClient;
    private final ProgressAccessClient progressClient;
    private final CompletionEngineClient completionClient;

    //Only used for testing, get it from contentClient in real
    public Set<String> getTopicLabels(String topicId) {
        return Set.of("1", "2", "3");
    }
    
    //Only used for testing, get from progressClient in real
    public Set<String> getClickedLabels(String learnerId, String topicId) {
        return Set.of("1"); 
    }

    public ProgressService(ContentAccessClient contentClient,
                       ProgressAccessClient progressClient,
                       CompletionEngineClient completionClient) {
        this.contentClient = contentClient;
        this.progressClient = progressClient;
        this.completionClient = completionClient;
    }

    public void recordClick(String learnerId, String topicId, String label) {
        System.out.println("Calling ProgressAccess...");
        progressClient.recordClick(learnerId, topicId, label);
        Set<String> allLabels = getTopicLabels(topicId);
        Set<String> clickedLabels = getClickedLabels(learnerId, topicId);
        System.out.println("Calling CompletionEngine...");
        CompletionResponse result = completionClient.checkCompletion(allLabels, clickedLabels);
        System.out.println("Completion result: " + result);

        if ("COMPLETE".equals(result.getStatus())) {
            System.out.println(">>> Sending TEST_EVENT...");
            rabbitTemplate.convertAndSend(
                "topic-completed-exchange",
                "completed.test",
                "TEST_EVENT"
            );
        }
    }

    public Object getProgress(String learnerId) {
        return progressClient.getProgress(learnerId);
    }
}