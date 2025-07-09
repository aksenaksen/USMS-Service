package io.aksenaksen.demo.usms.member.domain;

import io.aksenaksen.demo.usms.member.domain.dto.MemberUpdateProfileRequest;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MemberTest {

    Member member;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        // Initialize any common test data or configurations here
        passwordEncoder = MemberFixture.createPasswordEncoder();
        member = Member.createOfEmail(MemberFixture.createMemberRegisterRequest("ans109905@naver.com"),passwordEncoder);
    }

    @Test
    void create(){
        System.out.println(member.toString());
        Assertions.assertThat(member.getId()).isNotNull();
        Assertions.assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
        Assertions.assertThat(member.getRole()).isEqualTo(MemberRole.OWNER);
    }
    @Test
    void active(){
        member.activate();
        Assertions.assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    void failActive(){
        member.activate();
        Assertions.assertThatThrownBy(() -> member.activate()
                ).isInstanceOf(IllegalStateException.class);
        ;
    }

    @Test
    void failDeActive(){
        Assertions.assertThatThrownBy(() -> member.deactivate())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void deActive(){
        member.activate();
        member.deactivate();
        Assertions.assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    @Test
    void ChangeProfile(){
        member.activate();
        member.changeProfile(new MemberUpdateProfileRequest("hello new nickname", "010-9876-5432"));
        Assertions.assertThat(member.getProfile().nickname()).isEqualTo("hello new nickname");
        Assertions.assertThat(member.getProfile().phoneNumber()).isEqualTo("010-9876-5432");
    }

    @Test
    void changePassword(){
        member.changePassword("newSecret", passwordEncoder);
        Assertions.assertThat(passwordEncoder.matches("newSecret", member.getPassword())).isTrue();
    }
}