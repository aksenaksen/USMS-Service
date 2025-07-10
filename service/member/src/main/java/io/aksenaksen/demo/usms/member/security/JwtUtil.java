package io.aksenaksen.demo.usms.member.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    private final Key key;

    private static final long ACCESS_EXPIRATION_TIME = TimeUnit.HOURS.toMillis(1);
    private static final long REFRESH_EXPIRATION_TIME = TimeUnit.DAYS.toMillis(1);


    // 리터럴 키 상수
    private static final String CLAIM_TOKEN_TYPE = "tokenType";
    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_ROLE = "role";

    public JwtUtil(@Value("${jwt.secret-key}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String createToken(String tokenType, String username, String userId, String role) {
        return Jwts.builder()
                .setClaims(createClaims(tokenType, username, userId, role))
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() +
                                (tokenType.equals(TokenType.ACCESS.name())
                                        ? ACCESS_EXPIRATION_TIME : REFRESH_EXPIRATION_TIME)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims createClaims(String tokenType, String username, String userId, String role) {
        Map<String, Object> claimMap = Map.of(
                CLAIM_TOKEN_TYPE, tokenType,
                CLAIM_EMAIL, username,
                CLAIM_USER_ID, userId,
                CLAIM_ROLE, role
        );
        Claims claims = Jwts.claims();
        claims.putAll(claimMap);
        return claims;
    }

    public String getTokenType(String token) {
        return getClaim(token, CLAIM_TOKEN_TYPE);
    }

    public String getUserId(String token) {
        return getClaim(token, CLAIM_USER_ID);
    }

    public String getRole(String token) {
        return getClaim(token, CLAIM_ROLE);
    }

    public String getEmail(String token) {
        return getClaim(token, CLAIM_EMAIL);
    }

    public boolean isExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    private String getClaim(String token, String key) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get(key, String.class);
    }
}
