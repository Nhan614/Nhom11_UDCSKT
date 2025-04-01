package com.example.shareEdu.controller;


import com.example.shareEdu.dto.request.AuthenticationRequest;
import com.example.shareEdu.dto.response.AuthenticationResponse;
import com.example.shareEdu.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class AuthenticationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthenticationService authenticationService;

    private AuthenticationRequest authenticationRequest;
    private AuthenticationResponse authenticationResponse;


    @BeforeEach
    public void initData(){

        authenticationRequest = AuthenticationRequest.builder()
                .userName("hieuhieu")
                .password("12345678")
                .build();

        authenticationResponse = AuthenticationResponse.builder()
                .token("bbvbfjbjvdjfvjdfvjdvfdjvjf")
                .authenticated(true)
                .build();
    }

    @Test
    public void authentication_validRequest_success() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(authenticationRequest);

        //GIVEN
        Mockito.when(authenticationService.authenticate(ArgumentMatchers.any(AuthenticationRequest.class)))
                .thenReturn(authenticationResponse);


        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("result.token").value("bbvbfjbjvdjfvjdfvjdvfdjvjf"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.authenticated").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("code")
                        .value(1000))


        ;



        //THEN

    }



}
