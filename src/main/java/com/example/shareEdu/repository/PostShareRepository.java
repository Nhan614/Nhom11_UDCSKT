package com.example.shareEdu.repository;

import com.example.shareEdu.entity.PostShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostShareRepository extends JpaRepository<PostShare, Long> {
}
