package io.aksenaksen.demo.usms.member.domain;

import io.aksenaksen.demo.usms.member.domain.dto.MemberRegisterOfEmailRequest;
import org.springframework.test.util.ReflectionTestUtils;

public class MemberFixture {

    public static MemberRegisterOfEmailRequest createMemberRegisterRequest(String mail) {
        return new MemberRegisterOfEmailRequest(mail, "secret", "hello","010-1234-5678");
    }

    public static MemberRegisterOfEmailRequest createMemberRegisterRequest() {
        return new MemberRegisterOfEmailRequest("ans109905@naver.com", "secret","james", "010-1234-5678");
    }

    public static PasswordEncoder createPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };
    }

    public static Member createMember(Long id){
        Member member = Member.createOfEmail(createMemberRegisterRequest(),createPasswordEncoder());
        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }
}