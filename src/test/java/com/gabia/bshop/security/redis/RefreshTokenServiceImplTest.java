package com.gabia.bshop.security.redis;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.gabia.bshop.exception.UnAuthorizedException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceImplTest {

    @InjectMocks
    RefreshTokenServiceImpl refreshTokenRedisService;

    @Mock
    RefreshTokenRepository refreshTokenRepository;

    private final String tokenValue = "tokenValue";
    private final Long memberId = 1L;

    @Test
    void 리프레시_토큰을_저장한다() {
        // given
        final RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(tokenValue)
                .expiredAt(LocalDateTime.now())
                .memberId(memberId)
                .build();
        given(refreshTokenRepository.findById(refreshToken.refreshToken())).willReturn(
                Optional.of(refreshToken));
        // when & then
        assertThatThrownBy(() -> refreshTokenRedisService.save(refreshToken))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 저장된_리프레시_토큰이_없을_때_예외를_던진다() {
        // given
        final RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(tokenValue)
                .expiredAt(LocalDateTime.now())
                .memberId(memberId)
                .build();
        // when
        given(refreshTokenRepository.findById(refreshToken.refreshToken())).willReturn(
                Optional.empty());
        // then
        assertThatThrownBy(() -> refreshTokenRedisService.findToken(refreshToken.refreshToken()))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 존재하지_않는_리프레시_토큰을_삭제하면_예외를_던진다() {
        // given
        final RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(tokenValue)
                .expiredAt(LocalDateTime.now())
                .memberId(memberId)
                .build();
        // when
        given(refreshTokenRepository.findById(refreshToken.refreshToken())).willReturn(
                Optional.empty());
        // then
        assertThatThrownBy(() -> refreshTokenRedisService.findToken(refreshToken.refreshToken()))
                .isInstanceOf(UnAuthorizedException.class);
    }
}
