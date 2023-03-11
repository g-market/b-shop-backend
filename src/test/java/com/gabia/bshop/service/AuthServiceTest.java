package com.gabia.bshop.service;

import static com.gabia.bshop.exception.ErrorCode.*;
import static com.gabia.bshop.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gabia.bshop.config.ImageDefaultProperties;
import com.gabia.bshop.dto.response.AdminLoginResponse;
import com.gabia.bshop.dto.response.HiworksProfileResponse;
import com.gabia.bshop.dto.response.IssuedTokensResponse;
import com.gabia.bshop.dto.response.LoginResult;
import com.gabia.bshop.entity.Member;
import com.gabia.bshop.entity.enumtype.MemberRole;
import com.gabia.bshop.exception.ForbiddenException;
import com.gabia.bshop.exception.UnAuthorizedException;
import com.gabia.bshop.exception.UnAuthorizedRefreshTokenException;
import com.gabia.bshop.mapper.HiworksProfileMapper;
import com.gabia.bshop.mapper.MemberResponseMapper;
import com.gabia.bshop.repository.MemberRepository;
import com.gabia.bshop.repository.RefreshTokenRepository;
import com.gabia.bshop.security.RefreshToken;
import com.gabia.bshop.security.client.HiworksOauthClient;
import com.gabia.bshop.security.provider.JwtProvider;
import com.gabia.bshop.security.provider.RefreshTokenProvider;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	private final LocalDateTime expiredAt = LocalDateTime.now().plusDays(14);
	private final Long memberId = 1L;
	@InjectMocks
	private AuthService authService;
	@Mock
	private HiworksOauthClient hiworksOauthClient;
	@Mock
	private MemberRepository memberRepository;
	@Mock
	private JwtProvider jwtProvider;
	@Mock
	private RefreshTokenProvider refreshTokenProvider;
	@Mock
	private RefreshTokenRepository refreshTokenRepository;
	@Mock
	private ImageDefaultProperties imageDefaultProperties;

	@Test
	@DisplayName("하이웍스 인증 코드가 들어왔을 때 회원정보가 없으면 회원 정보를 저장하고 회원 정보와 토큰을 반환한다")
	void given_authCodeAndFirst_when_login_then_then_return_loginResult() {
		// given
		final String authCode = "authCode";
		final String accessToken = "accessToken";
		final String applicationToken = "applicationToken";
		final String refreshTokenValue = "refreshToken";
		final HiworksProfileResponse hiworksProfileResponse = JAIME.hiworksProfileResponse();
		final Member member = JAIME.getInstance(memberId);
		final RefreshToken refreshToken = new RefreshToken(refreshTokenValue, memberId, expiredAt);

		given(hiworksOauthClient.getAccessToken(authCode))
			.willReturn(accessToken);
		given(hiworksOauthClient.getProfile(accessToken))
			.willReturn(hiworksProfileResponse);
		given(memberRepository.findByHiworksId(hiworksProfileResponse.hiworksId()))
			.willReturn(Optional.empty());
		given(
			memberRepository.save(HiworksProfileMapper.INSTANCE.hiworksProfileResponseToMember(hiworksProfileResponse)))
			.willReturn(member);
		given(jwtProvider.createAccessToken(member.getId(), member.getRole()))
			.willReturn(applicationToken);
		given(refreshTokenProvider.createToken(memberId))
			.willReturn(refreshToken);
		given(refreshTokenRepository.save(refreshToken)).willReturn(refreshToken);
		given(imageDefaultProperties.getProfileImageUrl()).willReturn("http://b-shop.com/profile/me");

		// when
		final LoginResult loginResult = authService.login(authCode);

		// then
		assertAll(
			() -> assertThat(loginResult.accessToken()).isEqualTo(applicationToken),
			() -> assertThat(loginResult.memberResponse()).isEqualTo(MemberResponseMapper.INSTANCE.from(member)),
			() -> assertThat(loginResult.refreshToken()).isEqualTo(refreshTokenValue),
			() -> verify(hiworksOauthClient).getAccessToken(authCode),
			() -> verify(hiworksOauthClient).getProfile(accessToken),
			() -> verify(memberRepository).findByHiworksId(hiworksProfileResponse.hiworksId()),
			() -> verify(memberRepository).save(
				HiworksProfileMapper.INSTANCE.hiworksProfileResponseToMember(hiworksProfileResponse)),
			() -> verify(jwtProvider).createAccessToken(memberId, member.getRole()),
			() -> verify(refreshTokenProvider).createToken(memberId),
			() -> verify(refreshTokenRepository).save(refreshToken)
		);
	}

	@Test
	@DisplayName("하이웍스 인증코드가 들어왔을때 회원정보가 있으면 이름을 업데이트하고 회원정보와 토큰을 반환한다")
	void given_authCode_when_login_then_then_return_loginResult() {
		// given
		final String authCode = "authCode";
		final String accessToken = "accessToken";
		final String applicationToken = "applicationToken";
		final String refreshTokenValue = "refreshToken";
		final RefreshToken refreshToken = new RefreshToken(refreshTokenValue, memberId, expiredAt);

		HiworksProfileResponse hiworksProfileResponse = JAIME.hiworksProfileResponse();
		Member member = JAIME.getInstance(memberId);
		given(hiworksOauthClient.getAccessToken(authCode))
			.willReturn(accessToken);
		given(hiworksOauthClient.getProfile(accessToken))
			.willReturn(hiworksProfileResponse);
		given(memberRepository.findByHiworksId(hiworksProfileResponse.hiworksId()))
			.willReturn(Optional.of(member));
		given(jwtProvider.createAccessToken(member.getId(), member.getRole()))
			.willReturn(applicationToken);
		given(refreshTokenProvider.createToken(memberId))
			.willReturn(refreshToken);
		given(refreshTokenRepository.save(refreshToken)).willReturn(refreshToken);
		given(imageDefaultProperties.getProfileImageUrl()).willReturn("http://b-shop.com/profile/me");

		// when
		final LoginResult loginResult = authService.login(authCode);

		// then
		assertAll(
			() -> assertThat(loginResult.accessToken()).isEqualTo(applicationToken),
			() -> assertThat(loginResult.memberResponse()).isEqualTo(MemberResponseMapper.INSTANCE.from(member)),
			() -> verify(hiworksOauthClient).getAccessToken(authCode),
			() -> verify(hiworksOauthClient).getProfile(accessToken),
			() -> verify(memberRepository).findByHiworksId(hiworksProfileResponse.hiworksId()),
			() -> verify(jwtProvider).createAccessToken(memberId, member.getRole()),
			() -> verify(refreshTokenProvider).createToken(memberId),
			() -> verify(refreshTokenRepository).save(refreshToken)
		);
	}

	@Test
	@DisplayName("하이웍스 코드가 들어왔을때 회원정보가 있고 어드민 계정이면 어드민 로그인을 진행하고 토큰을 반환한다")
	void given_authCode_when_loginAdmin_then_then_return_adminLoginResponse() {
		// given
		final String authCode = "authCode";
		final String accessToken = "accessToken";
		final String applicationToken = "applicationToken";

		HiworksProfileResponse hiworksProfileResponse = AIDEN.hiworksProfileResponse();
		Member ADMIN = AIDEN.getInstance(memberId);

		given(hiworksOauthClient.getAccessToken(authCode))
			.willReturn(accessToken);
		given(hiworksOauthClient.getProfile(accessToken))
			.willReturn(hiworksProfileResponse);
		given(memberRepository.findByHiworksId(hiworksProfileResponse.hiworksId()))
			.willReturn(Optional.of(ADMIN));
		given(jwtProvider.createAccessToken(ADMIN.getId(), ADMIN.getRole()))
			.willReturn(applicationToken);

		// when
		AdminLoginResponse adminLoginResponse = authService.loginAdmin(authCode);

		// then
		assertAll(
			() -> assertThat(adminLoginResponse.accessToken()).isEqualTo(applicationToken),
			() -> verify(hiworksOauthClient).getAccessToken(authCode),
			() -> verify(hiworksOauthClient).getProfile(accessToken),
			() -> verify(memberRepository).findByHiworksId(hiworksProfileResponse.hiworksId()),
			() -> verify(jwtProvider).createAccessToken(memberId, ADMIN.getRole())
		);
	}

	@Test
	@DisplayName("하이웍스 코드가 들어왔을때 하이웍스 유저이지만 어드민이 아닌경우 어드민 로그인을 실패하고 예외가 발생한다")
	void given_authCodeAndNotAdmin_when_loginAdmin_then_then_throw_exception() {
		// given
		final String authCode = "authCode";
		final String accessToken = "accessToken";

		HiworksProfileResponse hiworksProfileResponse = JAIME.hiworksProfileResponse();
		Member member = JAIME.getInstance(memberId);

		given(hiworksOauthClient.getAccessToken(authCode))
			.willReturn(accessToken);
		given(hiworksOauthClient.getProfile(accessToken))
			.willReturn(hiworksProfileResponse);
		given(memberRepository.findByHiworksId(hiworksProfileResponse.hiworksId()))
			.willReturn(Optional.of(member));

		// when & then
		assertAll(
			() -> Assertions.assertThatThrownBy(() -> authService.loginAdmin(authCode))
				.isExactlyInstanceOf(ForbiddenException.class),
			() -> verify(hiworksOauthClient).getAccessToken(authCode),
			() -> verify(hiworksOauthClient).getProfile(accessToken),
			() -> verify(memberRepository).findByHiworksId(hiworksProfileResponse.hiworksId())
		);
	}

	@Test
	@DisplayName("유효한 리프레시 토큰으로 액세스 토큰을 발급한다")
	void given_validRefreshToken_when_issueAccessToken_then_then_return_IssuedTokenResponse() {
		// given
		String refreshTokenValue = "validRefreshToken";
		String newAccessTokenValue = "newAccessToken";
		String newRefreshTokenValue = "newRefreshToken";
		RefreshToken refreshToken = new RefreshToken(refreshTokenValue, memberId, expiredAt);
		RefreshToken newRefreshToken = new RefreshToken(newRefreshTokenValue, memberId, expiredAt);

		given(refreshTokenRepository.findToken(refreshTokenValue))
			.willReturn(refreshToken);
		given(refreshTokenProvider.createToken(memberId))
			.willReturn(newRefreshToken);
		given(refreshTokenRepository.save(eq(newRefreshToken)))
			.willReturn(newRefreshToken);
		willDoNothing().given(refreshTokenRepository).delete(refreshTokenValue);

		given(jwtProvider.createAccessToken(1L, MemberRole.NORMAL))
			.willReturn(newAccessTokenValue);
		given(memberRepository.findById(1L))
			.willReturn(Optional.of(Member.builder().id(1L).role(MemberRole.NORMAL).build()));

		// when
		IssuedTokensResponse issuedTokensResponse = authService.issueAccessToken(refreshTokenValue);

		// then
		assertAll(
			() -> assertThat(issuedTokensResponse.accessToken()).isEqualTo(newAccessTokenValue),
			() -> assertThat(issuedTokensResponse.refreshToken()).isEqualTo(newRefreshTokenValue),
			() -> verify(refreshTokenRepository).findToken(refreshTokenValue),
			() -> verify(refreshTokenProvider).createToken(memberId),
			() -> verify(refreshTokenRepository).save(eq(newRefreshToken)),
			() -> verify(jwtProvider).createAccessToken(1L, MemberRole.NORMAL),
			() -> verify(refreshTokenRepository).delete(refreshTokenValue)
		);
	}

	@Test
	@DisplayName("저장되어 있지않은 리프레시 토큰으로 액세스 토큰 발급하려할 경우 예외 발생한다")
	void given_NotSavedRefreshToken_when_issueAccessToken_then_then_throw_exception() {
		// given
		given(refreshTokenRepository.findToken(any()))
			.willThrow(new UnAuthorizedException(REFRESH_TOKEN_NOT_FOUND_EXCEPTION));

		// when, then
		assertThatThrownBy(() -> authService.issueAccessToken("refreshToken"))
			.isExactlyInstanceOf(UnAuthorizedException.class);
	}

	@Test
	@DisplayName("만료된 리프레시 토큰으로 액세스 토큰 발급하려할 경우 예외 발생한다")
	void given_ExpiredRefreshToken_when_issueAccessToken_then_then_throw_exception() {
		// given
		String refreshTokenValue = "refreshToken";
		LocalDateTime expiredDate = LocalDateTime.now().minusDays(1);
		given(refreshTokenRepository.findToken(any()))
			.willReturn(new RefreshToken(refreshTokenValue, memberId, expiredDate));
		willDoNothing().given(refreshTokenRepository).delete(refreshTokenValue);

		// when, then
		assertAll(
			() -> assertThatThrownBy(() -> authService.issueAccessToken(refreshTokenValue))
				.isExactlyInstanceOf(UnAuthorizedRefreshTokenException.class),
			() -> verify(refreshTokenRepository).findToken(any()),
			() -> verify(refreshTokenRepository).delete(refreshTokenValue)
		);
	}
}
