package com.example.shareEdu.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    Post post;

    @ManyToOne
    User user;

    @Lob
    String content;

    LocalDateTime createdAt;

    @Column(nullable = false)
    Boolean isDelete = false;

}
