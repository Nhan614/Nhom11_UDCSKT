package com.example.shareEdu.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePostResponse {
    Long id;
    String content;
    LocalDateTime createdAt;
    Set<TopicResponse> topics;


}
