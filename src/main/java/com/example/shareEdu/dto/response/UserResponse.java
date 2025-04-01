package com.example.shareEdu.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {

    String id;
    String userName;
    String firstName;
    String lastName;
    LocalDate dob;
    String email;
    LocalDate createdAt;
    Set<RoleResponse> roles ;
}
