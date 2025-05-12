package com.example.shareEdu.controller;


import com.example.shareEdu.dto.request.PostCreateRequest;
import com.example.shareEdu.dto.request.PostShareCreateRequest;
import com.example.shareEdu.dto.response.ApiResponse;
import com.example.shareEdu.dto.response.PostResponse;
import com.example.shareEdu.dto.response.PostShareResponse;
import com.example.shareEdu.service.PostShareService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/postShare")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostShareController {
    PostShareService postShareService;

    @PostMapping
    public ApiResponse<PostShareResponse> createPostShare(@RequestBody PostShareCreateRequest request){
        return ApiResponse.<PostShareResponse>builder()
                .result(postShareService.createPostShare(request))
                .build();
    }

}
