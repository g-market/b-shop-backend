package com.gabia.bshop.dto.response;

import java.time.LocalDateTime;

import com.gabia.bshop.entity.enumtype.ItemStatus;

public record ItemReservationResponse(
	Long itemId,
	ItemStatus itemStatus,
	LocalDateTime openAt
) {
}
