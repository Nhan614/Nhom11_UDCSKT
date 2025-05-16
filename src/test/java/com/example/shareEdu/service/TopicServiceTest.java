package com.example.shareEdu.service;

import com.example.shareEdu.dto.response.TopicResponse;
import com.example.shareEdu.entity.Topic;
import com.example.shareEdu.exception.AppException;
import com.example.shareEdu.exception.ErrorCode;
import com.example.shareEdu.mapper.TopicMapper;
import com.example.shareEdu.repository.TopicRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class TopicServiceTest {

    @Autowired
    private TopicService topicService;

    @MockBean
    private TopicRepository topicRepository;

    @MockBean
    private TopicMapper topicMapper;

    private Topic topic;
    private TopicResponse topicResponse;

    @BeforeEach
    public void setup() {
        topic = Topic.builder()
                .id(1L)
                .name("Java Basics")
                .description("Intro to Java")
                .approved(false)
                .isDeleted(false)
                .build();

        topicResponse = TopicResponse.builder()
                .id(1L)
                .name("Java Basics")
                .description("Intro to Java")
                .build();
    }

    @Test
    public void approveTopic_validTopic_success() {
        // Arrange
        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));
        when(topicRepository.save(any(Topic.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(topicMapper.toTopicResponse(any(Topic.class))).thenReturn(topicResponse);

        // Act
        TopicResponse response = topicService.approveTopic(1L);

        // Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getId()).isEqualTo(1L);
        Assertions.assertThat(topic.isApproved()).isTrue(); // Check topic đã set approved true
    }

    @Test
    public void approveTopic_topicNotFound_throwsException() {
        when(topicRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> topicService.approveTopic(1L))
                .isInstanceOf(AppException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.TOPIC_NOT_EXITS);
    }

    @Test
    public void rejectTopic_validTopic_success() {
        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));
        when(topicRepository.save(any(Topic.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(topicMapper.toTopicResponse(any(Topic.class))).thenReturn(topicResponse);

        TopicResponse response = topicService.rejectTopic(1L);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(topic.isDeleted()).isTrue(); // Check topic đã set deleted true
    }

    @Test
    public void rejectTopic_topicNotFound_throwsException() {
        when(topicRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> topicService.rejectTopic(1L))
                .isInstanceOf(AppException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.TOPIC_NOT_EXITS);
    }
}

