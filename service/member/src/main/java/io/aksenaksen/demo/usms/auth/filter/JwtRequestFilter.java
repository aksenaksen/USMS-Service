package io.aksenaksen.demo.usms.auth.filter;

import io.aksenaksen.demo.jwt.JwtUtil;
import io.aksenaksen.demo.usms.auth.domain.CustomUserDetails;
import io.aksenaksen.demo.usms.auth.domain.TokenType;
import io.aksenaksen.demo.usms.member.domain.Member;
import io.aksenaksen.demo.usms.member.domain.MemberRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String EXPIRED_TOKEN_MESSAGE = "엑세스 토큰이 만료되었습니다.";
    private static final String INVALID_TOKEN_MESSAGE = "유효하지 않은 토큰입니다.";
    private static final String UNKNOWN_TOKEN_TYPE_MESSAGE = "알 수 없는 토큰 타입입니다.";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(TOKEN_PREFIX.length());

        if (isExpired(response, token) || isNotAccessToken(response, token)) return;

        String email = jwtUtil.getEmail(token);
        String role = jwtUtil.getRole(token);
        String memberId = jwtUtil.getUserId(token);

        Member member = Member.builder()
                .id(memberId)
                .role(MemberRole.from(role))
                .email(email)
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(member);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isExpired(HttpServletResponse response, String token) throws IOException {
        if (jwtUtil.isExpired(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().print(EXPIRED_TOKEN_MESSAGE);
            return true;
        }
        return false;
    }

    private boolean isNotAccessToken(HttpServletResponse response, String token) throws IOException {
        try {
            TokenType type = TokenType.valueOf(jwtUtil.getTokenType(token));
            if (type != TokenType.ACCESS) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().print(INVALID_TOKEN_MESSAGE);
                return true;
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().print(UNKNOWN_TOKEN_TYPE_MESSAGE);
            return true;
        }
        return false;
    }
}
