package com.gabia.bshop.security.redis;

import java.time.LocalDateTime;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Builder
@RedisHash(value = "refresh_token")
public record RefreshToken(@Id String refreshToken, Long memberId, LocalDateTime expiredAt) {

    public boolean isExpired() {
        return expiredAt.isBefore(LocalDateTime.now());
    }
}
