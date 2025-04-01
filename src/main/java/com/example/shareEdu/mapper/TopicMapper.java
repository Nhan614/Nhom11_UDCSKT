package com.example.shareEdu.mapper;

import com.example.shareEdu.dto.request.TopicRequest;
import com.example.shareEdu.dto.response.TopicResponse;
import com.example.shareEdu.entity.Topic;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface TopicMapper {

    Topic toTopic(TopicRequest topicRequest);

    TopicResponse toTopicResponse(Topic topic);
}
