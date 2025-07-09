package io.aksenaksen.demo.usms.member.application.provided;

import io.aksenaksen.demo.usms.member.application.required.MemberRepository;
import io.aksenaksen.demo.usms.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MemberFinder {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Member find(String memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));
    }

}
