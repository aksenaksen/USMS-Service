package io.aksenaksen.demo.usms.member.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.util.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> body = null;
        try {
            body = mapper.readValue(request.getInputStream(), Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String username = body.get("email");
        String password = body.get("password");

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult
    ) throws IOException {

        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();

        String username = userDetails.getMember().getEmail();
        String role = userDetails.getMember().getRole().getRoleWithPrefix();
        String userId = userDetails.getMember().getId();


        String accessToken = jwtUtil.createToken(TokenType.ACCESS.name(),username,userId,role);
        String refreshToken = jwtUtil.createToken(TokenType.REFRESH.name(),username,userId,role);

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer "+ accessToken);
        response.addCookie(createCookie(TokenType.REFRESH.name(), refreshToken));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    private Cookie createCookie(String name, String refreshToken) {
        Cookie cookie = new Cookie(name, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(1));
        return cookie;
    }
}
