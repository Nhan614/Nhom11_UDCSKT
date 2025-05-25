package com.example.shareEdu.exception;


import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.Objects;

import com.example.shareEdu.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;

import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import lombok.extern.slf4j.Slf4j;


//8.3.5, 8.4.5 @ControllerAdvice catche AppException
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception) {
        log.error("Exception: ", exception);

        //15.1.2.2: GlobalExceptionHandler bắt lấy lỗi và return ResponseEntity về cho AdminDashboardUI.
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    //8.3.6, 8.4.6, 16.2.2, 16.2.6, 16.2.10 @ExceptionHandler(AppException.class)
    //handlingApException(AppException)
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception) {

        //8.3.7, 8.4.7 getErrorCode()
        ErrorCode errorCode = exception.getErrorCode();

        //8.3.8, 8.4.8 new ApiResponse()
        ApiResponse apiResponse = new ApiResponse();
        //8.3.9, 8.4.9 setCode(errorCode.getCode());
        apiResponse.setCode(errorCode.getCode());
        //8.3.10, 8.4.10 setMessage(errorCode.getMessage())
        apiResponse.setMessage(errorCode.getMessage());

        //8.3.13, 8.4.13 return ResponseEntity<ApiResponse> with proper HTTP status
        return ResponseEntity
                //8.3.11, 8.4.11 status(ErrorCode.HttpStatus())
                .status(errorCode.getHttpStatus())
                //8.3.12, 8.4.12 body(apiResponse)
                .body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attributes = null;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
            var contrainViolation = exception.getBindingResult().getAllErrors()
                    .get(0).unwrap(ConstraintViolation.class);

            attributes = contrainViolation.getConstraintDescriptor().getAttributes();

            log.info(attributes.toString());


        } catch (IllegalArgumentException e) {

        }

        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(
                Objects.nonNull(attributes)
                        ? mapAttribute(errorCode.getMessage(), attributes)
                        : errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
    @ExceptionHandler(value = AuthorizationDeniedException.class)
    ResponseEntity<ApiResponse> handlingAuthorizationDeniedException(AuthorizationDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

}