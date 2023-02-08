package com.gabia.bshop.security.client;

import com.gabia.bshop.dto.response.HiworksProfileResponse;

import lombok.Builder;

@Builder
public record LocalHiworksOauthClient(
	String clientId,
	String secret,
	String accessTokenUrl,
	String profileUrl
) implements HiworksOauthClient {

	@Override
	public String getAccessToken(final String authCode) {
		if (authCode.startsWith("normal")) {
			return "normal" + authCode.substring(6, 8);
		}
		return "admin" + authCode.substring(5, 7);
	}

	@Override
	public HiworksProfileResponse getProfile(final String accessToken) {
		return HiworksProfileResponse.builder()
			.hiworksId(accessToken)
			.email(accessToken + "@gabia.com")
			.name(accessToken)
			.build();
	}
}
