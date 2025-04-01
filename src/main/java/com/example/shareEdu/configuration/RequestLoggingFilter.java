package com.example.shareEdu.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, jakarta.servlet.ServletException {
        // Log request
        logger.info("Incoming Request: {} {}", request.getMethod(), request.getRequestURI());

        // Log headers
        logger.info("Headers:");
        Collections.list(request.getHeaderNames()).forEach(header ->
                logger.info("  {}: {}", header, request.getHeader(header)));

        // Tiếp tục chuỗi filter
        filterChain.doFilter(request, response);

        // Log response status
        logger.info("Response Status: {}", response.getStatus());
    }
}