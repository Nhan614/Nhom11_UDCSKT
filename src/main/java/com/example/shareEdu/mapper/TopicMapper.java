package com.example.shareEdu.mapper;

import com.example.shareEdu.dto.request.TopicRequest;
import com.example.shareEdu.dto.response.TopicResponse;
import com.example.shareEdu.entity.Topic;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface TopicMapper {

    Topic toTopic(TopicRequest topicRequest);

//    16.1.1.11  trả về Topic đã lưu dưới dạng dto
    TopicResponse toTopicResponse(Topic topic);
}
