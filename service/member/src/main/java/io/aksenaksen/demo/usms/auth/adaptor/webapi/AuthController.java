package io.aksenaksen.demo.usms.auth.adaptor.webapi;

import io.aksenaksen.demo.jwt.JwtUtil;
import io.aksenaksen.demo.usms.auth.application.provieded.RefreshTokenPort;
import io.aksenaksen.demo.usms.auth.domain.Expiration;
import io.aksenaksen.demo.usms.auth.domain.TokenType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final RefreshTokenPort refreshTokenPort;

    @PostMapping("/v1/auth/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = extractRefreshToken(request);

        String errorMessage = validateRefreshToken(refreshToken);
        if (errorMessage != null) {
            return ResponseEntity.badRequest().body(errorMessage);
        }

        String username = jwtUtil.getEmail(refreshToken);
        String role = jwtUtil.getRole(refreshToken);
        String userId = jwtUtil.getUserId(refreshToken);

        String accessToken = jwtUtil.createToken(TokenType.ACCESS.name(), username, userId, role, Expiration.ACCESS.getTtl());
        String newRefreshToken = jwtUtil.createToken(TokenType.REFRESH.name(),username,userId,role, Expiration.REFRESH.getTtl());

// 토큰 재발급하면서 redis 에 값 삭제 후 업데이트
        refreshTokenPort.reissueToken(username,newRefreshToken,Expiration.REFRESH.getTtl());

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        response.addCookie(createCookie(TokenType.REFRESH.name(), newRefreshToken));

        return ResponseEntity.ok().build();
    }



    private String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> TokenType.REFRESH.name().equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    private String validateRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            return "리프레시 토큰이 필요합니다.";
        }

        if (jwtUtil.isExpired(refreshToken)) {
            return "리프레시 토큰이 만료되었습니다.";
        }

        if (!TokenType.REFRESH.name().equals(jwtUtil.getTokenType(refreshToken))) {
            return "유효하지 않은 토큰입니다.";
        }

        return null; // 유효한 경우 null 반환
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