package com.example.shareEdu.repository;

import com.example.shareEdu.entity.PostShare;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostShareRepository extends JpaRepository<PostShare, Long> {
    @EntityGraph(attributePaths = {"post.topics"})
    List<PostShare> findAll();
}
