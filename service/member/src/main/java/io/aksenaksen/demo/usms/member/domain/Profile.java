package io.aksenaksen.demo.usms.member.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public record Profile(
        String nickname,
        String phoneNumber
) {

    private static final String PHONE_NUM_PATTERN = "^01[0-9]-\\d{3,4}-\\d{4}$";

    public Profile {
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("닉네임은 비어있을수 없습니다");
        }
        if (phoneNumber == null || !phoneNumber.matches(PHONE_NUM_PATTERN)) {
            throw new IllegalArgumentException("휴대폰 번호 형식이 올바르지 않습니다");
        }
    }

    public static Profile create(String nickname, String phoneNumber) {
        return new Profile(nickname, phoneNumber);
    }
}
