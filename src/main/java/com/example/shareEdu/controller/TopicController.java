package com.example.shareEdu.controller;

import com.example.shareEdu.dto.response.ApiResponse;
import com.example.shareEdu.dto.response.TopicResponse;
import com.example.shareEdu.service.TopicService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TopicController {

    TopicService topicService;

    // Duyệt topic
    @PutMapping("/{id}/approve")
    public ApiResponse<TopicResponse> approveTopic(@PathVariable Long id) {
        TopicResponse response = topicService.approveTopic(id);
        return ApiResponse.<TopicResponse>builder()
                .result(response)
                .build();
    }

    // Từ chối topic
    @PutMapping("/{id}/reject")
    public ApiResponse<TopicResponse> rejectTopic(@PathVariable Long id) {
        TopicResponse response = topicService.rejectTopic(id);
        return ApiResponse.<TopicResponse>builder()
                .result(response)
                .build();
    }
}
