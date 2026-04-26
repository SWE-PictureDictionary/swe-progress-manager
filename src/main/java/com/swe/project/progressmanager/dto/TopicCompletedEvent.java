package com.swe.project.progressmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicCompletedEvent {
    private String learnerId;
    private String topicId;
    private int clickedCount;
    private int totalLabels;
    private LocalDateTime completedAt;
}
