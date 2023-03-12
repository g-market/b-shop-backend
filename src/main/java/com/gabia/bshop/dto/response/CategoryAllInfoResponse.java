package com.gabia.bshop.dto.response;

public record CategoryAllInfoResponse(
	Long id,
	String name,
	boolean deleted
) {
}
