package io.aksenaksen.demo.usms.member.application.provided;

import io.aksenaksen.demo.usms.member.domain.Member;
import io.aksenaksen.demo.usms.member.domain.dto.MemberRegisterOfEmailRequest;
import io.aksenaksen.demo.usms.member.domain.dto.MemberUpdateProfileRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceImplTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    private MemberServiceImpl memberServiceImpl;

    @Test
    @DisplayName("정상적으로 회원 등록이 가능하다")
    void registerReturnMemberWhenRequestIsValid() {
        MemberRegisterOfEmailRequest request = new MemberRegisterOfEmailRequest(
                "test@example.com", "password", "닉네임", "010-1234-5678"
        );
        Member member = memberServiceImpl.register(request);
        assertNotNull(member);
        assertEquals("test@example.com", member.getEmail());
        assertEquals("닉네임", member.getProfile().nickname());
        assertEquals("010-1234-5678", member.getProfile().phoneNumber());
    }

    @Test
    @DisplayName("존재하는 회원을 조회할 수 있다")
    void findReturnMemberWhenMemberIdExists() {
        MemberRegisterOfEmailRequest request = new MemberRegisterOfEmailRequest(
                "findtest@example.com", "password", "닉네임", "010-1111-2222"
        );
        Member created = memberServiceImpl.register(request);
        Member found = memberServiceImpl.find(created.getId());
        assertNotNull(found);
        assertEquals(created.getId(), found.getId());
        assertEquals("findtest@example.com", found.getEmail());
    }

    @Test
    @DisplayName("회원 정보 수정이 정상 동작한다")
    void updateReturnUpdatedMemberWhenRequestIsValid() {
        MemberRegisterOfEmailRequest request = new MemberRegisterOfEmailRequest(
                "updatetest@example.com", "password", "닉네임", "010-2222-3333"
        );
        Member created = memberServiceImpl.register(request);
        memberServiceImpl.activate(created.getId());

        MemberUpdateProfileRequest updateRequest = new MemberUpdateProfileRequest("새닉네임", "010-9999-8888");
        Member updated = memberServiceImpl.update(created.getId(), updateRequest);
        assertNotNull(updated);
        assertEquals("새닉네임", updated.getProfile().nickname());
        assertEquals("010-9999-8888", updated.getProfile().phoneNumber());
    }

    @Test
    @DisplayName("존재하지 않는 회원 조회시 예외 발생")
    void findThrowExceptionWhenMemberIdDoesNotExist() {
        assertThrows(RuntimeException.class, () -> memberServiceImpl.find("not-exist-id"));
    }
}