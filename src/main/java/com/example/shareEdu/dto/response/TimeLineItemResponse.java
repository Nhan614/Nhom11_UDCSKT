package com.example.shareEdu.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimeLineItemResponse {
    String type;
    LocalDateTime time;
    PostResponse post;
    PostShareResponse share;
}
