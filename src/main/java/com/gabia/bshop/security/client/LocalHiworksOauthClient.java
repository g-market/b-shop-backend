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

	private static final int NORMAL_BEGIN_INDEX = 6;
	private static final int NORMAL_END_INDEX = 6;
	private static final int ADMIN_BEGIN_INDEX = 6;
	private static final int ADMIN_END_INDEX = 6;

	@Override
	public String getAccessToken(final String authCode) {
		if (authCode.startsWith("normal")) {
			return "normal" + authCode.substring(NORMAL_BEGIN_INDEX, NORMAL_END_INDEX);
		}
		return "admin" + authCode.substring(ADMIN_BEGIN_INDEX, ADMIN_END_INDEX);
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
