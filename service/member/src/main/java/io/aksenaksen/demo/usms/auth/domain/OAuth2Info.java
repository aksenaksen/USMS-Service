package io.aksenaksen.demo.usms.auth.domain;

public interface OAuth2Info {
    String getProviderId();
    String getProvider();
    String getName();
    String getEmail();
    String getMobNo();
}
