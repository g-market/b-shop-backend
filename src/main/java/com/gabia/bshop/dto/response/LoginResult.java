package com.gabia.bshop.dto.response;

import com.gabia.bshop.entity.Member;

public record LoginResult(
	String refreshToken,
	String accessToken,
	Member member
) {

}
