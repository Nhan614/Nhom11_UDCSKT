package com.example.shareEdu.repository;

import com.example.shareEdu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUserName(String username);

    Optional<User> findByUserName(String username);



}
