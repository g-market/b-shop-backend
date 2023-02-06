package com.gabia.bshop.security.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.gabia.bshop.security.redis.RefreshToken;
import org.junit.jupiter.api.Test;

class RefreshTokenProviderTest {

    @Test
    void 리프레시_토큰을_생성한다() {
        // given
        RefreshTokenProvider provider = new RefreshTokenProvider(1209600000);
        // when
        RefreshToken token = provider.createToken(1L);
        //then
        assertAll(
                () -> assertThat(token.isExpired()).isFalse(),
                () -> assertThat(token.refreshToken()).isNotNull()
        );
    }
}
