package com.example.shareEdu.service;

import com.example.shareEdu.dto.request.*;
import com.example.shareEdu.dto.response.AuthenticationResponse;
import com.example.shareEdu.dto.response.IntrospectResponse;
import com.example.shareEdu.entity.InvalidatedToken;
import com.example.shareEdu.entity.Role;
import com.example.shareEdu.entity.User;
import com.example.shareEdu.exception.AppException;
import com.example.shareEdu.exception.ErrorCode;
import com.example.shareEdu.repository.InvalidatedTokenRepository;
import com.example.shareEdu.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.text.ParseException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private InvalidatedTokenRepository invalidatedTokenRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = User.builder()
                .userName("testuser")
                .password(new BCryptPasswordEncoder().encode("password"))
                .roles(Collections.singleton(Role.builder().name("USER").build()))
                .build();

        // Set up giá trị mặc định cho các biến configuration
      //  authenticationService.SIGNER_KEY = "thisisaverylongsecretkeyforhs512whichisdefinitelymorethan64byteslong1234567890";
        authenticationService.VALID_DURATION = 3600; // 1 giờ
        authenticationService.REFRESHABLE_DURATION = 86400; // 1 ngày
    }

    @Test
    void authenticate_success() {
        // Arrange
        AuthenticationRequest request = AuthenticationRequest.builder()
                .userName("testuser")
                .password("password")
                .build();

        when(userRepository.findByUserName("testuser")).thenReturn(Optional.of(testUser));

        // Act
        AuthenticationResponse response = authenticationService.authenticate(request);

        // Assert
        assertNotNull(response);
        assertTrue(response.isAuthenticated());
        assertNotNull(response.getToken());
        verify(userRepository).findByUserName("testuser");
    }

    @Test
    void authenticate_userNotFound_throwsException() {
        // Arrange
        AuthenticationRequest request = AuthenticationRequest.builder()
                .userName("unknown")
                .password("password")
                .build();

        when(userRepository.findByUserName("unknown")).thenReturn(Optional.empty());

        // Act & Assert
        AppException exception = assertThrows(AppException.class,
                () -> authenticationService.authenticate(request));
        assertEquals(ErrorCode.USER_EXISTED, exception.getErrorCode());
    }

    @Test
    void authenticate_wrongPassword_throwsException() {
        // Arrange
        AuthenticationRequest request = AuthenticationRequest.builder()
                .userName("testuser")
                .password("wrongpassword")
                .build();

        when(userRepository.findByUserName("testuser")).thenReturn(Optional.of(testUser));

        // Act & Assert
        AppException exception = assertThrows(AppException.class,
                () -> authenticationService.authenticate(request));
        assertEquals(ErrorCode.UNAUTHENTICATED, exception.getErrorCode());
    }

    @Test
    void introspect_validToken() throws JOSEException, ParseException {
        // Arrange
        String token = authenticationService.genarateToken(testUser);
        IntrospectRequest request = IntrospectRequest.builder()
                .token(token)
                .build();

        // Act
        IntrospectResponse response = authenticationService.introspect(request);

        // Assert
        assertNotNull(response);
        assertTrue(response.isValid());
    }

    @Test
    void logout_success() throws JOSEException, ParseException {
        // Arrange
        String token = authenticationService.genarateToken(testUser);
        LogoutRequest request = LogoutRequest.builder()
                .token(token)
                .build();

        when(invalidatedTokenRepository.save(any(InvalidatedToken.class)))
                .thenReturn(InvalidatedToken.builder().build());

        // Act
        authenticationService.logout(request);

        // Assert
        verify(invalidatedTokenRepository).save(any(InvalidatedToken.class));
    }

    @Test
    void refreshToken_success() throws JOSEException, ParseException {
        // Arrange
        String oldToken = authenticationService.genarateToken(testUser);
        RefreshTokenRequest request = RefreshTokenRequest.builder()
                .token(oldToken)
                .build();

        when(userRepository.findByUserName("testuser")).thenReturn(Optional.of(testUser));
        when(invalidatedTokenRepository.save(any(InvalidatedToken.class)))
                .thenReturn(InvalidatedToken.builder().build());

        // Act
        AuthenticationResponse response = authenticationService.refreshToken(request);

        // Assert
        assertNotNull(response);
        assertTrue(response.isAuthenticated());
        assertNotNull(response.getToken());
        assertNotEquals(oldToken, response.getToken());
        verify(invalidatedTokenRepository).save(any(InvalidatedToken.class));
    }

    @Test
    void refreshToken_invalidToken_throwsException() {
        // Arrange
        RefreshTokenRequest request = RefreshTokenRequest.builder()
                .token("invalid.token.here")
                .build();

        // Act & Assert
        assertThrows(AppException.class,
                () -> authenticationService.refreshToken(request));
    }
}