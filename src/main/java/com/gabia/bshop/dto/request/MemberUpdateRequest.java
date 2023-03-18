package com.gabia.bshop.dto.request;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record MemberUpdateRequest(
	@NotNull(message = "핸드폰 번호를 기입해주세요.")
	@Pattern(regexp = "^010(\\d){4}(\\d){4}$", message = "핸드폰 번호를 올바르게 기입해주세요.")
	String phoneNumber,

	@URL
	String profileImageUrl
) {
}
