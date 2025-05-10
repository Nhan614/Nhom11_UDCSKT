package com.example.shareEdu.service;

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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class PostShareServiceTest {

    @Autowired
    private PostShareService postShareService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private PostShareRepository postShareRepository;

    @MockBean
    private PostShareMapper postShareMapper;

    private PostShareCreateRequest request;
    private User user;
    private Post post;
    private PostShare postShare;
    private PostShareResponse postShareResponse;

    @BeforeEach
    public void init() {
        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("john");

        SecurityContext context = org.mockito.Mockito.mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);

        request = PostShareCreateRequest.builder()
                .postId(1L)
                .caption("This is a shared post")
                .build();

        user = User.builder()
                .id("user-id-123")
                .userName("john")
                .build();

        post = Post.builder()
                .id(1L)
                .content("Original Post")
                .build();

        postShare = PostShare.builder()
                .id(99L)
                .content(request.getCaption())
                .shareAt(LocalDateTime.now())
                .user(user)
                .post(post)
                .build();

        postShareResponse = PostShareResponse.builder()
                .id(99L)
                .post(post)
                .build();
    }

    @Test
    public void createPostShare_validRequest_success() {
        // GIVEN
        when(userRepository.findByUserName("john")).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postShareRepository.save(any(PostShare.class))).thenReturn(postShare);
        when(postShareMapper.toPostShareResponse(any(PostShare.class))).thenReturn(postShareResponse);

        // WHEN
        PostShareResponse response = postShareService.createPostShare(request);

        // THEN
        Assertions.assertThat(response.getId()).isEqualTo(99L);
        Assertions.assertThat(response.getCaption()).isEqualTo("This is a shared post");
    }

    @Test
    public void createPostShare_userNotFound_throwException() {
        // GIVEN
        when(userRepository.findByUserName("john")).thenReturn(Optional.empty());

        // WHEN
        AppException exception = assertThrows(AppException.class, () -> postShareService.createPostShare(request));

        // THEN
        Assertions.assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_EXISTED);
    }

    @Test
    public void createPostShare_postNotFound_throwException() {
        // GIVEN
        when(userRepository.findByUserName("john")).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        // WHEN
        AppException exception = assertThrows(AppException.class, () -> postShareService.createPostShare(request));

        // THEN
        Assertions.assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.POST_NOT_FOUND);
    }
}
