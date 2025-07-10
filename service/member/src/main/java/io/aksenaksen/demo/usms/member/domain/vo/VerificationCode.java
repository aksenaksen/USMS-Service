package io.aksenaksen.demo.usms.member.domain.vo;

import java.util.Random;

public record VerificationCode(
        String principal,
        String code
) {

    public static VerificationCode createCode(String principal){
        return new VerificationCode(principal, generateCode());
    }
    public static VerificationCode createCode(String principal,String code){
        return new VerificationCode(principal, code);
    }

    private static String generateCode(){
        Random random = new Random();
        int num = 100000 + random.nextInt(900000);
        return String.valueOf(num);
    }

    public boolean verify(String inputCode){
        return code.equals(inputCode);
    }

}
