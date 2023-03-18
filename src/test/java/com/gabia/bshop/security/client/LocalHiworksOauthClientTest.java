package com.gabia.bshop.security.client;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gabia.bshop.dto.response.HiworksProfileResponse;

class LocalHiworksOauthClientTest {

	final LocalHiworksOauthClient localHiworksOauthClient = LocalHiworksOauthClient.builder()
		.clientId("clientId")
		.secret("secret")
		.build();

	final String normalAuthCode = "normal01authCodeauthCodeauthCodeauthCode";
	final String adminAuthCode = "admin01authCodeauthCodeauthCodeauthCode";

	@Test
	@DisplayName("accessToken은_authCode의_접두사_기준으로_생성한다")
	void given_access_token_when_getProfileUsingAccessToken_then_return_normalMemberProfile() {
		// when & then
		assertAll(
			() -> assertThat(localHiworksOauthClient.getAccessToken(normalAuthCode))
				.isEqualTo("normal01"),
			() -> assertThat(localHiworksOauthClient.getAccessToken(adminAuthCode))
				.isEqualTo("admin01")
		);
	}

	@Test
	@DisplayName("하이웍스 AccessToken을 사용하여 Normal 사용자 ProfileResponse를 생성한다")
	void givenAccessToken_whenGetProfileUsingAccessToken_thenCreateNormalMemberProfile() {
		// given
		final String normalAccessToken = localHiworksOauthClient.getAccessToken(normalAuthCode);
		// when
		final HiworksProfileResponse normalProfile = localHiworksOauthClient.getProfile(
			normalAccessToken);
		// then
		assertThat(normalProfile.hiworksId()).isEqualTo("normal01");
		assertThat(normalProfile.email()).isEqualTo("normal01@gabia.com");
		assertThat(normalProfile.name()).isEqualTo("normal01");
	}

	@Test
	@DisplayName("하이웍스 AccessToken을 사용하여 Admin 사용자 ProfileResponse를 생성한다")
	void givenAccessToken_whenGetProfileUsingAccessToken_thenCreateAdminMemberProfile() {
		// given
		final String adminAccessToken = localHiworksOauthClient.getAccessToken(adminAuthCode);
		// when
		final HiworksProfileResponse adminProfile = localHiworksOauthClient.getProfile(
			adminAccessToken);
		// then
		assertThat(adminProfile.hiworksId()).isEqualTo("admin01");
		assertThat(adminProfile.email()).isEqualTo("admin01@gabia.com");
		assertThat(adminProfile.name()).isEqualTo("admin01");
	}

}
