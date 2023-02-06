package com.gabia.bshop.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record HiworksProfileResponse(
	@JsonProperty("no")
	String hiworksId,

	@JsonProperty("user_id")
	String email,

	@JsonProperty("name")
	String name
) {
}
