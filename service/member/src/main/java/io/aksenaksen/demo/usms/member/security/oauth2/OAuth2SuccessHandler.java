package io.aksenaksen.demo.usms.member.security.oauth2;

import io.aksenaksen.demo.usms.member.security.CustomUserDetails;
import io.aksenaksen.demo.usms.member.security.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = userDetails.getMember().getEmail();
        String role = userDetails.getMember().getRole().getRoleWithPrefix();
        String userId = userDetails.getMember().getId();
        String token = jwtUtil.createToken(username,userId,role);


        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer "+ token);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(token);
    }
}
