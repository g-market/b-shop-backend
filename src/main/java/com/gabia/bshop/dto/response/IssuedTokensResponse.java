package com.gabia.bshop.dto.response;

import lombok.Builder;

@Builder
public record IssuedTokensResponse(
	String accessToken,
	String refreshToken
) {

}
