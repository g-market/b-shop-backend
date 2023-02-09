package com.gabia.bshop.dto;

import lombok.Builder;

@Builder
public record MemberDto(
	Long id,
	String email
) {
}
