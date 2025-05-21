package com.example.shareEdu.service;

import com.example.shareEdu.dto.response.PostResponse;
import com.example.shareEdu.dto.response.PostShareResponse;
import com.example.shareEdu.dto.response.TimeLineItemResponse;
import com.example.shareEdu.dto.response.TopicResponse;
import com.example.shareEdu.entity.Post;
import com.example.shareEdu.entity.PostShare;
import com.example.shareEdu.entity.Topic;
import com.example.shareEdu.mapper.PostMapper;
import com.example.shareEdu.mapper.PostShareMapper;
import com.example.shareEdu.mapper.TopicMapper;
import com.example.shareEdu.repository.PostRepository;
import com.example.shareEdu.repository.PostShareRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TimeLineService {
    PostRepository postRepository;
    PostShareRepository postShareRepository;
    PostMapper postMapper;
    PostShareMapper postShareMapper;
    TopicMapper topicMapper;

    public List<TimeLineItemResponse> getTimeLineItems() {
        List<TimeLineItemResponse> timelines = new ArrayList<>();

        timelines.addAll(postRepository.findAll().stream()
                .map(post -> {
                    PostResponse postResponse = postMapper.toPostResponse(post);
                    Set<TopicResponse> topicResponses = post.getTopics().stream()
                            .map(topicMapper::toTopicResponse)
                            .collect(Collectors.toSet());


                    return TimeLineItemResponse.builder()
                            .type("POST")
                            .time(post.getCreatedAt())
                            .post(postResponse)
                            .build();
                })
                .collect(Collectors.toList())
        );

        timelines.addAll(postShareRepository.findAll().stream()
                .map(share -> TimeLineItemResponse.builder()
                        .type("SHARE")
                        .time(share.getShareAt())
                        .share(postShareMapper.toPostShareResponse(share))
                        .build())
                .collect(Collectors.toList())
        );

        timelines.sort(Comparator.comparing(TimeLineItemResponse::getTime).reversed());

        return timelines;
    }

}
