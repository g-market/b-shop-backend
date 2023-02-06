package com.gabia.bshop.security.redis;

public interface RefreshTokenService {

    RefreshToken save(RefreshToken refreshToken);

    RefreshToken findToken(String savedTokenValue);

    void delete(String savedTokenValue);
}
