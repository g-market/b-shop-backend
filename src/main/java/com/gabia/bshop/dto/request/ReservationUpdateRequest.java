package com.gabia.bshop.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record ReservationUpdateRequest(
	@NotNull(message = "openAt 은 필수 값 입니다.")
	LocalDateTime openAt
) {
}
