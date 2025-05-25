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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TopicService {

    TopicRepository topicRepository;
    TopicMapper topicMapper;

    // Duyệt topic
    public TopicResponse approveTopic(Long topicId) {
        // 16.1.1.3. Gọi findById(topicId) để tìm topic theo ID
        Topic topic = topicRepository.findById(topicId)
                // 16.2.0: Nếu không tồn tại
                // 16.2.1 ném exception TOPIC_NOT_EXITS
                .orElseThrow(() -> new AppException(ErrorCode.TOPIC_NOT_EXITS));

        // 16.1.1.5. Kiểm tra topic đã được duyệt trước đó chưa
        if (topic.isApproved()) {
            // 16.2.5: Nếu topic đã được duyệt trước đó, ném exception TOPIC_ALREADY_APPROVED
            throw new AppException(ErrorCode.TOPIC_ALREADY_APPROVED);
        }
        // 16.1.1.6. Kiểm tra topic đã được duyệt trước đó chưa
        if (topic.isDeleted()) {
            // 16.2.9: Nếu topic đã được xóa trước đó, ném exception TOPIC_ALREADY_DELETED
            throw new AppException(ErrorCode.TOPIC_ALREADY_DELETED);
        }

        // 16.1.1.7: Đánh dấu topic là đã được duyệt
        topic.setApproved(true);

        // 16.1.1.8: Lưu topic đã cập nhật
        Topic updated = topicRepository.save(topic);

        // 16.1.10: gọi toTopicResponse(topic)
        return topicMapper.toTopicResponse(updated);
//        16.1.1.12  trả về Topic đã lưu dưới dạng dto
    }

    // Từ chối topic
    public TopicResponse rejectTopic(Long topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new AppException(ErrorCode.TOPIC_NOT_EXITS));

        topic.setDeleted(true);

        Topic updated = topicRepository.save(topic);

        return topicMapper.toTopicResponse(updated);
    }


    public List<TopicResponse> getAllTopics() {

        List<Topic> topics = topicRepository.findAll();

        List<TopicResponse> topicResponses = new ArrayList<>();

        topics.forEach(topic -> topicResponses.add(topicMapper.toTopicResponse(topic)));
        return topicResponses;
    }

    public List<TopicResponse> getPendingTopics() {
        List<Topic> pendingTopics = topicRepository.findByApprovedFalseAndIsDeletedFalse();
        return pendingTopics.stream()
                .map(topicMapper::toTopicResponse)
                .collect(Collectors.toList());
    }


}
