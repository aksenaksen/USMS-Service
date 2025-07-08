package io.aksenaksen.demo.usms.member.domain.dto;

import io.aksenaksen.demo.usms.member.domain.Profile;

public record MemberRegisterOfEmailRequest(
        String email,
        String password,
        String nickname,
        String phoneNumber
) {
}
