package io.aksenaksen.demo.usms.member.domain.dto;

public record MemberUpdateProfileRequest(
        String nickname,
        String phoneNumber
) {
}
