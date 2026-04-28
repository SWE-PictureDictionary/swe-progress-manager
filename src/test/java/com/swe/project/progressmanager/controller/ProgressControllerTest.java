package com.swe.project.progressmanager.controller;

import com.swe.project.progressmanager.service.ProgressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProgressController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ProgressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProgressService progressService;

    @Test
    void recordClickCallsServiceAndReturnsOk() throws Exception {
        String requestBody = """
                {
                  "learnerId": "learner-1",
                  "topicId": "fruits",
                  "label": "Apple"
                }
                """;

        mockMvc.perform(post("/progress/click")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        verify(progressService).recordClick("learner-1", "fruits", "Apple");
    }

    @Test
    void getProgressReturnsLearnerProgress() throws Exception {
        doReturn(ResponseEntity.ok("[]"))
                .when(progressService)
                .getProgress("learner-1");

        mockMvc.perform(get("/progress/learner-1"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

        verify(progressService).getProgress("learner-1");
    }

    @Test
    void getProgressForTopicReturnsTopicProgress() throws Exception {
        doReturn(ResponseEntity.ok("[]"))
                .when(progressService)
                .getProgressForTopic("learner-1", "fruits");

        mockMvc.perform(get("/progress/learner-1/topic/fruits"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

        verify(progressService).getProgressForTopic("learner-1", "fruits");
    }

    @Test
    void undoLatestClickReturnsServiceResponse() throws Exception {
        doReturn(ResponseEntity.noContent().build())
                .when(progressService)
                .undoLatestClick("learner-1", "fruits");

        mockMvc.perform(delete("/progress/learner-1/topic/fruits/latest"))
                .andExpect(status().isNoContent());

        verify(progressService).undoLatestClick("learner-1", "fruits");
    }
}
