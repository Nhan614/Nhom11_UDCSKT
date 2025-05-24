package com.example.shareEdu.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class PostShare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull(message = "user not null")
    @ManyToOne
    User user;

    @NotNull(message = "post not null")
    @ManyToOne
    Post post;

    String content;

    @Column(nullable = false)
    boolean isDeleted = false;

    LocalDateTime shareAt;
}
