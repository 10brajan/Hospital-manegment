package com.example.zajecia7doktorki.config.security.jwt;

import com.example.zajecia7doktorki.model.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        if (Objects.nonNull(httpServletRequest.getAttribute("expired"))) {
            ApiError apiError = ApiError.builder()
                    .httpStatus(HttpStatus.UNAUTHORIZED)
                    .errors(String.valueOf(httpServletRequest.getAttribute("expired")))
                    .build();
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.getWriter().write(mapper.writeValueAsString(apiError));
        }
    }
}