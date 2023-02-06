package com.gabia.bshop.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record HiworksTokenResponse(
	@JsonProperty("access_token")
	String accessToken,

	@JsonProperty("refresh_token")
	String refreshToken,

	@JsonProperty("office_no")
	Long officeNo,

	@JsonProperty("user_no")
	Long user_no
) {

}