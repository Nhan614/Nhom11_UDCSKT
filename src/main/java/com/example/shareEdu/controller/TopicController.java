package com.example.shareEdu.controller;

import com.example.shareEdu.dto.response.ApiResponse;
import com.example.shareEdu.dto.response.TopicResponse;
import com.example.shareEdu.service.TopicService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topics")
@CrossOrigin(origins = "http://localhost:63342")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TopicController {

    TopicService topicService;

    // Lấy danh sách chưa duyệt
    @GetMapping("/pending")
    public List<TopicResponse> getPendingTopics() {
        return topicService.getPendingTopics(); // Bạn cần triển khai hàm này
    }

    // Duyệt topic
    @PutMapping("/{id}/approve")
    public ApiResponse<TopicResponse> approveTopic(@PathVariable Long id) {
        // 16.1.1.2 Gọi TopicService.approveTopic(id)
        TopicResponse response = topicService.approveTopic(id);
        // 16.1.1.13: Đóng gói response vào ApiResponse và trả về cho UI
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

    @GetMapping
    public ApiResponse<List<TopicResponse>> getTopics() {
        return ApiResponse.<List<TopicResponse>>builder()
                .result(topicService.getAllTopics())
                .build();
    }
}
