package com.gabia.bshop.dto.request;

import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

@Builder
public record CategoryCreateRequest(
	@Length(max = 255, message = "255자 이내로 입력해주세요.")
	@NotBlank(message = "name은 필수 값입니다.")
	String name
) {
}
