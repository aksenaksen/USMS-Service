package io.aksenaksen.demo.usms.member.application.required;

public interface VerificationPort {
    void create(String principal, String code);
    String read(String principal);
    void remove(String principal);
}
