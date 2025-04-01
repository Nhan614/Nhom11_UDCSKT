package com.example.shareEdu.dto.request;

import com.example.shareEdu.entity.Topic;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostCreateRequest {
    String content;
    Set<Long> topics;



}
