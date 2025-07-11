package io.aksenaksen.demo.usms.auth.filter;

import io.aksenaksen.demo.jwt.JwtUtil;
import io.aksenaksen.demo.usms.auth.application.provieded.RefreshTokenPort;
import io.aksenaksen.demo.usms.auth.domain.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class LogoutFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RefreshTokenPort refreshTokenPort;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(!request.getRequestURI().endsWith("logout")|| !request.getMethod().equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals(TokenType.REFRESH.name())) {
                refresh = cookie.getValue();
            }
        }

        if(refresh == null || jwtUtil.isExpired(refresh) || !jwtUtil.getTokenType(refresh).equals(TokenType.REFRESH.name())) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }

        String username = jwtUtil.getEmail(refresh);
        if(!refresh.equals(refreshTokenPort.read(username))) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }

        refreshTokenPort.delete(refresh);

        response.setStatus(HttpStatus.OK.value());
        response.addCookie(createCookie(TokenType.REFRESH.name()));
    }

    private Cookie createCookie(String name) {
        Cookie cookie = new Cookie(name, null);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }
}
