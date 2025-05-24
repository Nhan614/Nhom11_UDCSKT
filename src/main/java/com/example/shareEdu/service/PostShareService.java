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

        //8.1.17 getContext().getAuthentication().getName()
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        //8.1.18 findByUsername(username) of UserRepository to get the currently logged in use
        User user = userRepository.findByUserName(username)
                // 8.3.4 throw AppException if not found(8.3.1 Optional.empty())
                .orElseThrow(() ->

                        //8.3.3 new AppException(ErrorCode.USER_NOT_EXIST)
                        new AppException(
                                //8.3.2 get USER_NOT_EXIST enum
                        ErrorCode.USER_NOT_EXISTED));

        //8.1.19 call findUserByName(username) of PostRepository to the post by id
        Post post = postRepository.findById(request.getPostId())
                // 8.4.4 throw AppException if not found(8.4.1 Optional.empty())
                .orElseThrow(() ->
                        //8.4.3 new AppException(ErrorCode.POST_NOT_FOUND)
                        new AppException(
                                //8.4.2 get USER_NOT_EXIST enum
                                ErrorCode.POST_NOT_FOUND));

        //8.1.20. PostShare Service calls PostShare's constructor Post Share(post, user, caption) to create a postShare object.
        PostShare postShare = PostShare.builder()
                .shareAt(LocalDateTime.now())
                .content(request.getCaption())
                .post(post)
                .user(user)
                .build();


        return postShareMapper
                //8.1.22.PostShareService calls the toPostShareResponse(postShare) method of the PostShareMapper to convert to postShareResponse.
                .toPostShareResponse(postShareRepository
                        .save(postShare)); //8.1.21 PostShareService calls the save(postShare) method of PostShareRepository to save the postShare to the database.,

    }
}
