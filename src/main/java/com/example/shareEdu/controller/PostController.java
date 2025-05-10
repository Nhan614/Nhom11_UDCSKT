package com.example.shareEdu.controller;


import com.example.shareEdu.dto.request.UpdatePostRequest;
import com.example.shareEdu.dto.response.ApiResponse;
import com.example.shareEdu.dto.request.PostCreateRequest;
import com.example.shareEdu.dto.response.PostResponse;
import com.example.shareEdu.entity.Post;
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
        Post p = postService.getPost(postId);
        postService.softDeletePost(p);
        return ApiResponse.<Void>builder().result(null).build();
    }

    @PutMapping
    public ApiResponse<PostResponse> updatePost(@RequestBody UpdatePostRequest request){
        return ApiResponse.<PostResponse>builder()
                .result(postService.updatePost(request)).build();
    }
}
