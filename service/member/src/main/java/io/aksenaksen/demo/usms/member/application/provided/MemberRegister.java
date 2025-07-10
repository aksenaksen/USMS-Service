package io.aksenaksen.demo.usms.member.application.provided;

import io.aksenaksen.demo.usms.member.application.required.MemberPort;
import io.aksenaksen.demo.usms.member.domain.Member;
import io.aksenaksen.demo.usms.member.domain.PasswordEncoder;
import io.aksenaksen.demo.usms.member.domain.dto.MemberRegisterOfEmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
public class MemberRegister {

    private final MemberPort memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member register(MemberRegisterOfEmailRequest request){
        Member member = Member.createOfEmail(request,passwordEncoder);

        memberRepository.save(member);

        return member;
    }
}
