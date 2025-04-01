package com.example.shareEdu.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    User author;

    @Lob
    String content;

    @ManyToMany
    Set<Topic> topics;

    @Column(nullable = false)
    boolean isDeleted = false;


    @ManyToMany
    Set<User> likedUsers;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<PostShare> shares;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)

    Set<Comment> comments;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;


}
