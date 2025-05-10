package com.example.shareEdu.dto.response;


import com.example.shareEdu.entity.Post;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class PostShareResponse {
    Long id;
    Post post;
    String caption;

}
