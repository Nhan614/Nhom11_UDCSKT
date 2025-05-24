package com.example.shareEdu.repository;


import com.example.shareEdu.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    // 16.1.1.4 Trả về topic
    // 16.1.1.9 trả về Topic đã lưu

    //
    List<Topic> findByApprovedFalseAndIsDeletedFalse();

}
