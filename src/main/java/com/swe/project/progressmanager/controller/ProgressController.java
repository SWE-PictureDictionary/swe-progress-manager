package com.swe.project.progressmanager.controller;

import com.swe.project.progressmanager.dto.ClickRequest;
import com.swe.project.progressmanager.dto.CompletionRequest;
import com.swe.project.progressmanager.dto.CompletionResponse;
import com.swe.project.progressmanager.service.ProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/progress")
public class ProgressController {

    private final ProgressService service;

    public ProgressController(ProgressService service) {
        this.service = service;
    }

    @PostMapping("/click")
    public ResponseEntity<Void> recordClick(@RequestBody ClickRequest request) {
        service.recordClick(request.getLearnerId(), request.getTopicId(), request.getLabel());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{learnerId}/topic/{topicId}/complete")
    public ResponseEntity<?> checkCompletion(@PathVariable String learnerId,
                                             @PathVariable String topicId) {
        CompletionRequest completionRequest = service.buildCompletionRequest(learnerId, topicId);
        CompletionResponse result = service.checkCompletion(completionRequest);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{learnerId}")
    public ResponseEntity<?> getProgress(@PathVariable String learnerId) {
        return service.getProgress(learnerId);
    }

    @GetMapping("/{learnerId}/topic/{topicId}")
    public ResponseEntity<?> getProgressForTopic(@PathVariable String learnerId,
                                                 @PathVariable String topicId) {
        return service.getProgressForTopic(learnerId, topicId);
    }

    @DeleteMapping("/{learnerId}/topic/{topicId}/latest")
    public ResponseEntity<?> undoLatestClick(@PathVariable String learnerId,
                                             @PathVariable String topicId) {
        return service.undoLatestClick(learnerId, topicId);
    }
}