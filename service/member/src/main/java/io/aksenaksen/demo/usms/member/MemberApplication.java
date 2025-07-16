package io.aksenaksen.demo.usms.member;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "io.aksenaksen.demo.usms.member",
        "io.aksenaksen.demo.notification",
        "io.aksenaksen.demo.jwt",
        "io.aksenaksen.demo.usms.auth",
        "io.aksenaksen.demo.usms.config"
})
public class MemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemberApplication.class, args);
    }

}
