package com.example.shareEdu.controller;


import com.example.shareEdu.dto.response.ApiResponse;
import com.example.shareEdu.dto.response.RoleResponse;
import com.example.shareEdu.dto.response.TimeLineItemResponse;
import com.example.shareEdu.service.TimeLineService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/timeline")
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TimelineController {
    TimeLineService timeLineService;

    @GetMapping
    public ApiResponse<List<TimeLineItemResponse>> getTimeline() {
        return ApiResponse.<List<TimeLineItemResponse>>builder()
                .result(timeLineService.getTimeLineItems())
                .build();
    }
}
