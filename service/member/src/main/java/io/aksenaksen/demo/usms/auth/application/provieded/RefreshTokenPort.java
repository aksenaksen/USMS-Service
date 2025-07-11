package io.aksenaksen.demo.usms.auth.application.provieded;

public interface RefreshTokenPort {

    public void delete(String key);
    public void create(String username, String refreshToken, long ttl);
    public void reissueToken(String username, String refreshToken, long ttl);
}
