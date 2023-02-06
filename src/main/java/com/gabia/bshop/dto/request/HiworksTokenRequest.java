package com.gabia.bshop.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record HiworksTokenRequest(
	@JsonProperty("client_id")
	String clientId,

	@JsonProperty("client_secret")
	String clientSecret,

	@JsonProperty("grant_type")
	String grantType,

	@JsonProperty("auth_code")
	String authCode,

	@JsonProperty("access_type")
	String accessType
) {

}
