package io.aksenaksen.demo.usms.member.application.provided;

import io.aksenaksen.demo.usms.member.domain.Member;
import io.aksenaksen.demo.usms.member.domain.dto.MemberRegisterOfEmailRequest;
import io.aksenaksen.demo.usms.member.domain.dto.MemberUpdateProfileRequest;

public interface MemberServicePort {

    Member register(MemberRegisterOfEmailRequest request);

    Member find(String memberId);

    Member update(String memberId, MemberUpdateProfileRequest request);

    Member activate(String memberId);
}
