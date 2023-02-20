package com.gabia.bshop.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record ReservationChangeRequest(

	@NotNull(message = "openAt은 필수 값 입니다.")
	LocalDateTime openAt
) {
}
