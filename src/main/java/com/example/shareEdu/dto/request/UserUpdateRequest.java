package com.example.shareEdu.dto.request;
import com.example.shareEdu.validator.DobContraint;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String password;
    String firstName;
    String lastName;
    LocalDate dob;

    @NotNull(message = "")
    @DobContraint(min = 18, message = "INVALID_DOB")
    List<String> roles;



}