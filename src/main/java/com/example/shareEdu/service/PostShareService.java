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

        //8.1.7 getUsername form Authentication
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        //8.1.8 call findUserByName(username) of UserRepository to get the currently logged in use
        User user = userRepository.findByUserName(username)
                .orElseThrow(() ->new AppException(ErrorCode.USER_NOT_EXISTED)); //if not found(8.2.1. return null)  -> 8.2.2. throw USER_NOT_EXISTED exception

        //8.1.9 call findUserByName(username) of PostRepository to the post by id
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() ->new AppException(ErrorCode.POST_NOT_FOUND)); //if not found -> (8.3.1 return null) ->  8.3.2 throw POST_NOT_FOUND exception

        //8.1.10. PostShare Service calls PostShare's constructor Post Share(post, user, caption) to create a postShare object.
        PostShare postShare = PostShare.builder()
                .shareAt(LocalDateTime.now())
                .content(request.getCaption())
                .post(post)
                .user(user)
                .build();

        return postShareMapper
                .toPostShareResponse(postShareRepository //8.1.12.PostShareService calls the toPostShareResponse(postShare) method of the PostShareMapper to convert to postShareResponse.
                        .save(postShare)); //8.1.11 PostShareService calls the save(postShare) method of PostShareRepository to save the postShare to the database.,

    }
}
