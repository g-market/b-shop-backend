package com.gabia.bshop.security.provider;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gabia.bshop.entity.enumtype.MemberRole;
import com.gabia.bshop.global.UnAuthorizedException;
import com.gabia.bshop.util.AuthTokenExtractor;
import org.junit.jupiter.api.Test;

class JwtProviderTest {

    private final JwtProvider jwtProvider = JwtProvider.builder()
            .authTokenExtractor(new AuthTokenExtractor())
            .secretKey("TESTSECRETKEYTESTSECRETKEYTESTSECRETKEYTESTSECRETKEY")
            .validityInMilliseconds(3600000)
            .build();

    private final FakeJwtProvider fakeJwtProvider = new FakeJwtProvider(
            "TESTSECRETKEYTESTSECRETKEYTESTSECRETKEYTESTSECRETKEY", 3600000);

    @Test
    void 토큰을_생성한다() {
        // given
        final Long memberId = 1L;
        // when
        final String token = jwtProvider.createAccessToken(memberId, MemberRole.NORMAL);
        // then
        assertThat(token).isNotNull();
    }

    @Test
    void 토큰이_유효한_경우() {
        // given
        String accessToken = jwtProvider.createAccessToken(1L, MemberRole.NORMAL);
        String authorizationHeader = "Bearer " + accessToken;

        // when & then
        assertThat(jwtProvider.isValidToken(authorizationHeader)).isTrue();
    }

    @Test
    void 토큰의_유효기간이_지난_경우() {
        // given
        JwtProvider jwtProvider = JwtProvider.builder()
                .authTokenExtractor(new AuthTokenExtractor())
                .secretKey("TESTSECRETKEYTESTSECRETKEYTESTSECRETKEYTESTSECRETKEY")
                .build();
        String accessToken = jwtProvider.createAccessToken(1L, MemberRole.NORMAL);
        String authorizationHeader = "Bearer " + accessToken;

        // when, then
        assertThat(jwtProvider.isValidToken(authorizationHeader)).isFalse();
    }

    @Test
    void 토큰의_형식이_틀린_경우() {
        // given
        String authorizationHeader = "Bearer invalidToken";

        // when, then
        assertThat(jwtProvider.isValidToken(authorizationHeader)).isFalse();
    }

    @Test
    void 토큰의_시크릿_키가_틀린_경우() {
        // given
        JwtProvider invalidJwtProvider = JwtProvider
                .builder()
                .authTokenExtractor(new AuthTokenExtractor())
                .secretKey("INVALIDTESTSECRETKEYTESTSECRETKEYTESTSECRETKEYTESTSECRETKEY")
                .validityInMilliseconds(3600000)
                .build();
        String token = invalidJwtProvider.createAccessToken(1L, MemberRole.NORMAL);
        String authorizationHeader = "Bearer " + token;
        // when & then
        assertThat(jwtProvider.isValidToken(authorizationHeader)).isFalse();
    }

    @Test
    void 토큰의_payload를_복호화한다() {
        // given
        String token = jwtProvider.createAccessToken(1L, MemberRole.NORMAL);
        String authorizationHeader = "Bearer " + token;
        // when
        MemberPayload payload = jwtProvider.getPayload(authorizationHeader);
        // then
        assertThat(payload).isEqualTo(new MemberPayload(1L, MemberRole.NORMAL));
    }

    @Test
    void 토큰의_payload_복호화_시_Long_id가_아니면_예외를_반환한다() {
        // given
        String token = fakeJwtProvider.createAccessToken("string", MemberRole.NORMAL);
        String authorizationHeader = "Bearer " + token;
        // when, then
        assertThatThrownBy(() -> jwtProvider.getPayload(authorizationHeader))
                .isExactlyInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 토큰의_payload_복호화_시_필요한_정보가_없으면_예외를_반환한다() {
        // given
        String accessToken = fakeJwtProvider.createAccessToken(1L);
        String authorizationHeader = "Bearer " + accessToken;

        // when, then
        assertThatThrownBy(() -> jwtProvider.getPayload(authorizationHeader))
                .isExactlyInstanceOf(UnAuthorizedException.class);
    }

    @Test
    void 토큰의_payload_복호화_시_role값이_올바르지_않으면_예외를_반환한다() {
        // given
        String token = fakeJwtProvider.createAccessToken(1L, "invalid");
        String authorizationHeader = "Bearer " + token;

        // when, then
        assertThatThrownBy(() -> jwtProvider.getPayload(authorizationHeader))
                .isExactlyInstanceOf(UnAuthorizedException.class);
    }
}