package io.aksenaksen.demo.usms.member.domain.dto;

public record MemberRegisterOfEmailRequest(
        String email,
        String password,
        String nickname,
        String phoneNumber
) {
}
