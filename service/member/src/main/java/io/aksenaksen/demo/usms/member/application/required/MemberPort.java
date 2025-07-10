package io.aksenaksen.demo.usms.member.application.required;

import io.aksenaksen.demo.usms.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberPort extends JpaRepository<Member, String> {

    Optional<Member> findByEmail(String email);
}
