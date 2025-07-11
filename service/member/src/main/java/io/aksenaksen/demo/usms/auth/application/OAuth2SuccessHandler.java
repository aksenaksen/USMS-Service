package io.aksenaksen.demo.usms.auth.application;

import io.aksenaksen.demo.jwt.JwtUtil;
import io.aksenaksen.demo.usms.auth.application.provieded.RefreshTokenPort;
import io.aksenaksen.demo.usms.auth.domain.CustomUserDetails;
import io.aksenaksen.demo.usms.auth.domain.Expiration;
import io.aksenaksen.demo.usms.auth.domain.TokenType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final RefreshTokenPort refreshTokenPort;

      @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = userDetails.getMember().getEmail();
        String role = userDetails.getMember().getRole().getRoleWithPrefix();
        String userId = userDetails.getMember().getId();
        String accessToken = jwtUtil.createToken(TokenType.ACCESS.name(), username,userId,role, Expiration.ACCESS.getTtl());
        String refreshToken = jwtUtil.createToken(TokenType.REFRESH.name(), username,userId,role, Expiration.REFRESH.getTtl());

        refreshTokenPort.create(username,refreshToken,Expiration.REFRESH.getTtl());

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer "+ accessToken);
        response.addCookie(createCookie(TokenType.REFRESH.name(), refreshToken));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(accessToken);
    }

    private Cookie createCookie(String name, String refreshToken) {
        Cookie cookie = new Cookie(name, refreshToken);
        cookie.setHttpOnly(true);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(1));
        return cookie;
    }

}
