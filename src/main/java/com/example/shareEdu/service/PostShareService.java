package com.example.shareEdu.service;


import com.example.shareEdu.dto.request.PostCreateRequest;
import com.example.shareEdu.dto.request.PostShareCreateRequest;
import com.example.shareEdu.dto.response.PostShareResponse;
import com.example.shareEdu.entity.Post;
import com.example.shareEdu.entity.PostShare;
import com.example.shareEdu.entity.User;
import com.example.shareEdu.exception.AppException;
import com.example.shareEdu.exception.ErrorCode;
import com.example.shareEdu.mapper.PostShareMapper;
import com.example.shareEdu.repository.PostRepository;
import com.example.shareEdu.repository.PostShareRepository;
import com.example.shareEdu.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostShareService {

    UserRepository userRepository;
    PostRepository postRepository;
    PostShareRepository postShareRepository;
    PostShareMapper postShareMapper;

    public PostShareResponse createPostShare(PostShareCreateRequest request) {
        //8.1.7 getUsername
        //8.1.8 return username
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        //8.1.9 findUserByName(username)
        //8.1.10. return current user
        User user = userRepository.findByUserName(username)
                .orElseThrow(() ->new AppException(ErrorCode.USER_NOT_EXISTED)); //if not found -> 8.2.2. throw USER_NOT_EXISTED exception



        //8.1.11 findUserByName(username)
        //8.1.12. return current user
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() ->new AppException(ErrorCode.POST_NOT_FOUND)); //if not found -> 8.3.1. throw POST_NOT_FOUND exception



        //8.1.13. new PostShare(user, post, caption)
        //8.1.14. return new  postShare object
        PostShare postShare = PostShare.builder()
                .shareAt(LocalDateTime.now())
                .content(request.getCaption())
                .post(post)
                .user(user)
                .build();

        //8.1.15. save(postShare)
        //8.1.16 return  postShare object
        // if failed ->  8.4.1. throw exception

        // 8.1.17. toPostShareResponse(postShare)
        //8.1.18. return postShareResponse
        return postShareMapper.toPostShareResponse(postShareRepository.save(postShare));

    }
}
