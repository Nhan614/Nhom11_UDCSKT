package com.example.shareEdu.controller;


import com.example.shareEdu.dto.response.ApiResponse;
import com.example.shareEdu.dto.request.PostCreateRequest;
import com.example.shareEdu.dto.response.PostResponse;
import com.example.shareEdu.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostController {
    PostService postService;

    @PostMapping
    public ApiResponse<PostResponse> createPost(@RequestBody PostCreateRequest request){
        return ApiResponse.<PostResponse>builder()
                .result(postService.createPost(request))
                .build();
    }

    @PatchMapping("/delete/{postId}")
    public ApiResponse<Void> deletePost(@PathVariable Long postId){
        postService.softDeletePost(postId);
        return ApiResponse.<Void>builder().result(null).build();
    }
}
