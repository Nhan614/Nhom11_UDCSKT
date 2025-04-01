package com.example.shareEdu.service;

import com.example.shareEdu.dto.request.UserCreationRequest;
import com.example.shareEdu.dto.response.UserResponse;
import com.example.shareEdu.entity.User;
import com.example.shareEdu.exception.AppException;
import com.example.shareEdu.repository.UserRepository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;


import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private LocalDate dob;
    private UserCreationRequest userCreationRequest;

    private UserResponse userResponse;
    private User user;


    @BeforeEach
    public void initData(){
        dob = LocalDate.of(2002, 1,1);
        userCreationRequest = UserCreationRequest.builder()
                .userName("john")
                .firstName("john")
                .lastName("doe")
                .password("12345678")
                .dob(dob)
                .build();

        userResponse = UserResponse.builder()
                .id("nvjhbdjfbvkd")
                .userName("john")
                .firstName("jojn")
                .lastName("doe")
                .dob(dob)
                .build();

        user = User.builder()
                .id("nvjhbdjfbvkd")
                .userName("john")
                .firstName("jojn")
                .lastName("doe")
                .dob(dob)
                .build();

    }

    @Test
    public void create_validRequest_success(){
        //GIVEN
        when(userRepository.existsByUserName(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        //When
        var response = userService.createUser(userCreationRequest);



        //THEN
        Assertions.assertThat(response.getId()).isEqualTo("nvjhbdjfbvkd");
        Assertions.assertThat(response.getUserName()).isEqualTo("john");

    }
    @Test
    public void create_validRequest_fail(){
        //GIVEN
        when(userRepository.existsByUserName(anyString())).thenReturn(true);

        //WHEN
        var exception = assertThrows(AppException.class, () -> userService.createUser(userCreationRequest));

        //THEN
        Assertions.assertThat(exception.getErrorCode().getCode())
                .isEqualTo(1002);
    }

}
