package com.gabia.bshop.security.provider;

import com.gabia.bshop.security.redis.RefreshToken;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenProvider {

    private final long expireLength;

    public RefreshTokenProvider(@Value("${token.refresh-expired-time}") final long expireLength) {
        this.expireLength = expireLength;
    }

    public RefreshToken createToken(final Long memberId) {
        final int days = (int) TimeUnit.MILLISECONDS.toDays(expireLength);
        final LocalDateTime expireDateTime = LocalDateTime.now().plusDays(days);
        return RefreshToken.builder()
                .refreshToken(UUID.randomUUID().toString())
                .memberId(memberId)
                .expiredAt(expireDateTime)
                .build();
    }
}