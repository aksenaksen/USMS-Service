package io.aksenaksen.demo.usms.member.application.provided;

import io.aksenaksen.demo.usms.member.application.exception.NotVerifiedException;
import io.aksenaksen.demo.usms.member.domain.Member;
import io.aksenaksen.demo.usms.member.domain.dto.MemberRegisterOfEmailRequest;
import io.aksenaksen.demo.usms.member.domain.dto.MemberUpdateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberServicePort {

    private final MemberRegister memberRegister;
    private final MemberEditor memberEditor;
    private final MemberFinder memberFinder;
    private final VerificationService verificationService;

    @Override
    public Member register(MemberRegisterOfEmailRequest request) {

        if(!verificationService.isVerified(request.email())) throw new NotVerifiedException("본인인증을 해야합니다.");

        return memberRegister.register(request);
    }

    @Override
    public Member find(String memberId) {
        return memberFinder.find(memberId);
    }

    @Override
    public Member update(String memberId,MemberUpdateProfileRequest request) {
        return memberEditor.updateProfile(memberId, request);
    }

    @Override
    public Member activate(String memberId) {
        return memberEditor.activate(memberId);
    }

}
