package com.gabia.bshop.security.client;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
	void accessToken은_authCode의_접두사_기준으로_생성한다() {
		// when & then
		assertAll(
			() -> assertThat(localHiworksOauthClient.getAccessToken(normalAuthCode))
				.isEqualTo("normal01"),
			() -> assertThat(localHiworksOauthClient.getAccessToken(adminAuthCode))
				.isEqualTo("admin01")
		);
	}

	@Test
	void accessToken을_사용하여_하이웍스_ProfileResponse를_생성한다() {
		// given
		final String normalAccessToken = localHiworksOauthClient.getAccessToken(normalAuthCode);
		final String adminAccessToken = localHiworksOauthClient.getAccessToken(adminAuthCode);

		// when
		final HiworksProfileResponse normalProfile = localHiworksOauthClient.getProfile(
			normalAccessToken);
		final HiworksProfileResponse adminProfile = localHiworksOauthClient.getProfile(
			adminAccessToken);

		// then
		assertThat(normalProfile.hiworksId()).isEqualTo("normal01");
		assertThat(normalProfile.email()).isEqualTo("normal01@gabia.com");
		assertThat(normalProfile.name()).isEqualTo("normal01");

		assertThat(adminProfile.hiworksId()).isEqualTo("admin01");
		assertThat(adminProfile.email()).isEqualTo("admin01@gabia.com");
		assertThat(adminProfile.name()).isEqualTo("admin01");
	}
}