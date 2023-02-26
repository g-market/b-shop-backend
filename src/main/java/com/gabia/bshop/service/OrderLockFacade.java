package com.gabia.bshop.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gabia.bshop.dto.OrderItemDto;
import com.gabia.bshop.dto.request.OrderCreateRequestDto;
import com.gabia.bshop.dto.response.OrderCreateResponseDto;
import com.gabia.bshop.entity.Order;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderLockFacade {

	private final OrderService orderService;

	public OrderCreateResponseDto purchaseOrder(final Long memberId,
		final OrderCreateRequestDto orderCreateRequestDto) {
		Order order = orderService.validateCreateOrder(memberId, orderCreateRequestDto);
		final List<OrderItemDto> sortedDtoList = orderCreateRequestDto.orderItemDtoList().stream()
			.sorted(Comparator.comparing(OrderItemDto::itemId).thenComparing(OrderItemDto::itemOptionId))
			.toList();

		OrderCreateResponseDto returnDto = orderService.lockCreateOrder(sortedDtoList, order);
		return returnDto;
	}
}
