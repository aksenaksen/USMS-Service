package io.aksenaksen.demo.usms.member.application.provided;

import io.aksenaksen.demo.usms.member.application.required.MemberPort;
import io.aksenaksen.demo.usms.member.domain.Member;
import io.aksenaksen.demo.usms.member.domain.dto.MemberUpdateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MemberEditor {

    private final MemberPort memberRepository;

    @Transactional
    public Member activate(String memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));

        member.activate();

        return memberRepository.save(member);
    }


    @Transactional
    public Member updateProfile(String memberId, MemberUpdateProfileRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));

        member.changeProfile(request);

        return memberRepository.save(member);
    }
}
