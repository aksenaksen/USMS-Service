package io.aksenaksen.demo.usms.member.application.required;

public interface VerificationStatusPort {
    void create(String principal);
    String read(String principal);
    void remove(String principal);
}
