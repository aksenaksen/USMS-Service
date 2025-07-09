package io.aksenaksen.demo.usms.member.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public record OAuthInfo(
        @Enumerated(EnumType.STRING) OAuthProvider provider,
        String oauthId
) {
}
