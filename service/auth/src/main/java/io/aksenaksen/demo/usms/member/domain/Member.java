package io.aksenaksen.demo.usms.member.domain;

import io.aksenaksen.demo.usms.member.domain.dto.MemberRegisterOfEmailRequest;
import io.aksenaksen.demo.usms.member.domain.dto.MemberUpdateProfileRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Entity
@ToString
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Embedded
    private Profile profile;

    @Embedded
    private OAuthInfo oAuthInfo;

    private LocalDateTime createdAt;

    @Builder
    public Member(String id, String email, String password, MemberStatus status, MemberRole role, Profile profile, OAuthInfo oAuthInfo, LocalDateTime createdAt) {
        this.id = requireNonNull(id);
        this.email = requireNonNull(email);
        this.password = requireNonNull(password);
        this.status = requireNonNull(status);
        this.role = requireNonNull(role);
        this.profile = requireNonNull(profile);
        this.oAuthInfo = oAuthInfo; // can be null
        this.createdAt = requireNonNull(createdAt);
    }




    public static Member createOfEmail(MemberRegisterOfEmailRequest request, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.id = UUID.randomUUID().toString();
        member.email = requireNonNull(request.email());
        member.password = requireNonNull(passwordEncoder.encode(request.password()));

        member.status = MemberStatus.PENDING;
        member.role = MemberRole.OWNER;

        member.profile = Profile.create(request.nickname(), request.phoneNumber());
        member.createdAt = LocalDateTime.now();
        return member;
    }

    public void activate() {
        Assert.state(status == MemberStatus.PENDING, "회원의 상태가 대기 상태여야 합니다.");
        this.status = MemberStatus.ACTIVE;
    }

    public void deactivate() {
        Assert.state(status == MemberStatus.ACTIVE, "회원의 상태가 활성화 상태여야합니다.");
        this.status = MemberStatus.DEACTIVATED;
    }

    public void changeProfile(MemberUpdateProfileRequest request){
        Assert.state(status == MemberStatus.ACTIVE, "회원의 상태가 활성화 상태여야 합니다.");
        this.profile = Profile.create(request.nickname(), request.phoneNumber());
    }

    public void changePassword(String password, PasswordEncoder passwordEncoder) {
        Assert.state(status == MemberStatus.ACTIVE, "회원의 상태가 활성화 상태여야 합니다.");
        this.password = passwordEncoder.encode(requireNonNull(password));
    }

    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }
}
