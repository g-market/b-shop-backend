package com.gabia.bshop.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabia.bshop.dto.response.OrderInfoPageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OrdersController {

	private static final int MAX_PAGE_ELEMENT_REQUEST_SIZE = 100;
	private final OrdersService orderService;

	// TODO: 인가 적용
	@GetMapping("/order-infos")
	public ResponseEntity<OrderInfoPageResponse> orderPagination(final Pageable pageable) {
		final Long memberId = 6L;

		validatePageElementSize(pageable);
		return ResponseEntity.ok(orderService.findOrdersPagination(memberId, pageable));
	}

	private void validatePageElementSize(final Pageable pageable) {
		if (pageable.getPageSize() > MAX_PAGE_ELEMENT_REQUEST_SIZE) {
			throw new IllegalArgumentException(
				String.format("상품은 한 번에 %d개 까지만 조회할 수 있습니다.", MAX_PAGE_ELEMENT_REQUEST_SIZE));
		}
	}
}
