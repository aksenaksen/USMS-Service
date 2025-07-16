package io.aksenaksen.demo.usms.member.adaptor.webapi;

import io.aksenaksen.demo.usms.member.application.provided.MemberServicePort;
import io.aksenaksen.demo.usms.member.domain.Member;
import io.aksenaksen.demo.usms.member.domain.dto.MemberRegisterOfEmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberServicePort memberService;

    @PostMapping("/v1/members")
    public ResponseEntity<Void> registerMember(
            @RequestBody MemberRegisterOfEmailRequest request
    ) {

        memberService.register(request);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/v1/members/{memberId}")
    public ResponseEntity<Member> findMember(
            @PathVariable("memberId") String memberId
    ){

        Member response = memberService.find(memberId);
        return ResponseEntity.ok(response);
    }


}
