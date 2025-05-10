package com.example.shareEdu.service;

import com.example.shareEdu.dto.request.PostCreateRequest;
import com.example.shareEdu.dto.response.PostResponse;
import com.example.shareEdu.dto.response.TopicResponse;
import com.example.shareEdu.entity.Post;
import com.example.shareEdu.entity.Topic;
import com.example.shareEdu.entity.User;
import com.example.shareEdu.repository.PostRepository;
import com.example.shareEdu.repository.TopicRepository;
import com.example.shareEdu.repository.UserRepository;
import lombok.With;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class PostServiceTest {

    @Autowired
    PostService postService;

    @MockBean
    PostRepository postRepository;

    @MockBean
    TopicRepository topicRepository;

    @MockBean
    UserRepository userRepository;


    Post post;

    User user;

    LocalDate dob;

    LocalDateTime createTime;

    Set<Topic> topics;

    PostCreateRequest postCreateRequest;

    PostResponse postResponse;

    Set<Long> topicIds;

    TopicResponse topicResponse1;
    TopicResponse topicResponse2;

    Set<TopicResponse> topicResponses;

    Topic topic1, topic2;


    @BeforeEach
    public void init(){
        topic1 =  Topic.builder()
                .name("Math")
                .id(12L)
                .build();
        topic2 =  Topic.builder()
                .name("History")
                .id(13L)
                .build();

        topics = Set.of(topic1, topic2);
        topicIds = Set.of(topic1.getId(), topic2.getId());


        dob = LocalDate.now();
        createTime = LocalDateTime.now();



        user = User.builder()
                .id("12345")
                .firstName("hieu")
                .userName("hieu")
                .lastName("Dinh")
                .address("abc")
                .email("h@gmial.com")
                .build();

        post = Post.builder()
                .id(10L)
                .author(user)
                .content("Hieu dep trai")
                .topics(topics)
                .createdAt(createTime)
               .build();

        postCreateRequest = PostCreateRequest.builder()
                .content("Hieu dep trai")
                .topics(topicIds)
                .build();

        topicResponse1 = TopicResponse.builder()
                .id(topic1.getId())
                .name(topic1.getName())
                .build();
        topicResponse2 = TopicResponse.builder()
                .id(topic2.getId())
                .name(topic2.getName())
                .build();

        topicResponses = Set.of(topicResponse1, topicResponse2);

        postResponse = PostResponse.builder()
                .content("Hieu dep trai")
                .createdAt(createTime)
                .topics(topicResponses)
                .build();



    }

    @Test
    public  void create_success(){
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("hieu");
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUserName("hieu")).thenReturn(Optional.of(user));
        when(topicRepository.findAllById(topicIds)).thenReturn(List.of(topic1,topic2));
        when(postRepository.save(any())).thenReturn(post);

        //
        var response = postService.createPost(postCreateRequest);

        //
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getId()).isEqualTo(post.getId());
        Assertions.assertThat(response.getContent()).isEqualTo("Hieu dep trai");
        Assertions.assertThat(response.getCreatedAt()).isEqualTo(createTime);
    }

    @Test
    @WithMockUser(username = "admin",roles={"ADMIN"})
    public void softDelete_success(){
        //given
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        //WHEN
        postService.softDeletePost(post);

        //THEN
        Assertions.assertThat(post.isDeleted()).isTrue();
    }

    @Test
    @WithMockUser(username = "hieu", authorities ={"DELETE_OWN_POST"})
    public void softDelete_ownPost_success(){
        //GIVEN
        post.setAuthor(user);
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        //WHEN
        postService.softDeletePost(post);

        //Then
        Assertions.assertThat(post.isDeleted()).isTrue();


    }
    @Test
    @WithMockUser(username = "user", authorities = {})
    public void softDelete_faild_noPermission(){
        //GIVEN
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        //THEN
        Assertions.assertThatThrownBy(() -> postService.softDeletePost(post))
                .isInstanceOf(AuthorizationDeniedException.class);
    }



}
