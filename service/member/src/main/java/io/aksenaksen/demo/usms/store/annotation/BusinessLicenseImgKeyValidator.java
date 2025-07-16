package io.aksenaksen.demo.usms.store.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

import static io.aksenaksen.demo.usms.store.constant.StoreConstants.BUSINESS_LICENSE_IMG_KEY_REGEX;


public class BusinessLicenseImgKeyValidator implements ConstraintValidator<BusinessLicenseImgKey, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if(value == null) return true;

        return Pattern.matches(BUSINESS_LICENSE_IMG_KEY_REGEX, value);
    }
}
