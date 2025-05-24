package com.example.shareEdu.service;

import com.example.shareEdu.dto.response.TopicResponse;
import com.example.shareEdu.entity.Topic;
import com.example.shareEdu.exception.AppException;
import com.example.shareEdu.exception.ErrorCode;
import com.example.shareEdu.mapper.TopicMapper;
import com.example.shareEdu.repository.TopicRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TopicService {

    TopicRepository topicRepository;
    TopicMapper topicMapper;

    // Duyệt topic
    public TopicResponse approveTopic(Long topicId) {
        // 16.1.1: Nhận topicId từ request và tìm topic theo ID
        Topic topic = topicRepository.findById(topicId)
                // 16.1.2: Nếu không tồn tại, ném exception
                .orElseThrow(() -> new AppException(ErrorCode.TOPIC_NOT_EXITS));

        // 16.1.3: Đánh dấu topic là đã được duyệt
        topic.setApproved(true);

        // 16.1.4: Lưu topic đã cập nhật
        Topic updated = topicRepository.save(topic);

        // 16.1.5: Chuyển entity sang DTO để trả về
        return topicMapper.toTopicResponse(updated);
    }

    // Từ chối topic
    public TopicResponse rejectTopic(Long topicId) {
        // 16.2.1: Nhận topicId từ request và tìm topic theo ID
        Topic topic = topicRepository.findById(topicId)
                // 16.2.2: Nếu không tồn tại, ném exception
                .orElseThrow(() -> new AppException(ErrorCode.TOPIC_NOT_EXITS));

        // 16.2.3: Đánh dấu topic là đã bị từ chối (xóa mềm)
        topic.setDeleted(true);

        // 16.2.4: Lưu topic đã cập nhật
        Topic updated = topicRepository.save(topic);

        // 16.2.5: Chuyển entity sang DTO để trả về
        return topicMapper.toTopicResponse(updated);
    }

    public List<TopicResponse> getAllTopics() {

        List<Topic> topics = topicRepository.findAll();

        List<TopicResponse> topicResponses = new ArrayList<>();

        topics.forEach(topic -> topicResponses.add(topicMapper.toTopicResponse(topic)));
        return topicResponses;
    }
}
