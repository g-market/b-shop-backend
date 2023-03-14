package com.gabia.bshop.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ItemImageCreateRequest(
	@NotNull(message = "imageNameList는 필수 값 입니다.")
	@Size(min = 1, message = "1개 이상의 item image를 입력하세요.")
	List<String> imageNameList
) {
}
