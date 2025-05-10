package com.example.shareEdu.service;


import com.example.shareEdu.dto.request.PostCreateRequest;
import com.example.shareEdu.dto.request.UpdatePostRequest;
import com.example.shareEdu.dto.response.PostResponse;
import com.example.shareEdu.entity.Post;
import com.example.shareEdu.entity.Topic;
import com.example.shareEdu.entity.User;
import com.example.shareEdu.exception.AppException;
import com.example.shareEdu.exception.ErrorCode;
import com.example.shareEdu.mapper.PostMapper;
import com.example.shareEdu.repository.PostRepository;
import com.example.shareEdu.repository.TopicRepository;
import com.example.shareEdu.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.naming.Context;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class PostService {

    UserRepository userRepository;
    PostRepository postRepository;
    TopicRepository topicRepository;
    PostMapper postMapper;

    public PostResponse createPost(PostCreateRequest postCreateRequest) {

        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();

        User author  = userRepository.findByUserName(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));


        Post post = postMapper.toPost(postCreateRequest);
        post.setCreatedAt(LocalDateTime.now());
        post.setAuthor(author);

       // log.info("request:..."+ postRepository.toString());
        log.info("topic from request : "+postCreateRequest.getTopics().toString());

        var topicsList = topicRepository.findAllById(postCreateRequest.getTopics());
        if(topicsList.isEmpty() || topicsList.size() < postCreateRequest.getTopics().size()) {
           throw new AppException(ErrorCode.TOPIC_NOT_EXITS);
        }
        log.info("list toppic: "+ topicsList);

        Set<Topic> topics = new HashSet<>(topicsList);

        post.setTopics(topics);
        log.info("topic: {}", topics);

        return postMapper.toPostResponse(postRepository.save(post));
    }
    public Post getPost(Long id){
        return postRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.POST_NOT_FOUND));
    }
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "(hasAuthority('DELETE_OWN_POST') and #post.author.id == authentication.principal.id)")
    public void softDeletePost(Post post){
        post.setDeleted(true);
        postRepository.save(post);
        log.info("Post with ID {} has been soft deleted", post.getId());


    }
    public PostResponse updatePost(UpdatePostRequest updatePostRequest) {
        Post post = getPost(updatePostRequest.getId());
        post.setUpdatedAt(LocalDateTime.now());

        postMapper.toPost(updatePostRequest);
        return postMapper.toPostResponse(postRepository.save(post));
    }
}
