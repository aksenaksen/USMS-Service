package io.aksenaksen.demo.usms.member.application.required;

public interface VerificationStatusRepository {
    void create(String principal);
    String read(String principal);
}
