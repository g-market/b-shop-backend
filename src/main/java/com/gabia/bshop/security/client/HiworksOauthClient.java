package com.gabia.bshop.security.client;

import com.gabia.bshop.dto.response.HiworksProfileResponse;

public interface HiworksOauthClient {

	String getAccessToken(final String authCode);

	HiworksProfileResponse getProfile(final String accessToken);
}
