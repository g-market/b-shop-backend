package com.gabia.bshop.controller;

import static com.gabia.bshop.exception.ErrorCode.*;

import com.gabia.bshop.dto.request.OrderInfoSearchRequest;
import com.gabia.bshop.dto.response.OrderInfoSingleResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gabia.bshop.dto.request.OrderCreateRequestDto;
import com.gabia.bshop.dto.response.OrderCreateResponseDto;
import com.gabia.bshop.dto.response.OrderInfoPageResponse;
import com.gabia.bshop.exception.ConflictException;
import com.gabia.bshop.service.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OrderController {

	private static final int MAX_PAGE_ELEMENT_REQUEST_SIZE = 100;
	private final OrderService orderService;

	// TODO: 인가 적용
	@GetMapping("/order-infos")
	public ResponseEntity<OrderInfoPageResponse> findOrders(final Pageable pageable) {
		final Long memberId = 6L;

		validatePageElementSize(pageable);
		return ResponseEntity.ok(orderService.findOrdersPagination(memberId, pageable));
	}
    // TODO: 인가 적용
    @GetMapping("/order-infos/{orderId}")
    public ResponseEntity<OrderInfoSingleResponse> singleOrderInfo(@PathVariable("orderId") final Long orderId) {
        final OrderInfoSingleResponse singleOrderInfo = orderService.findSingleOrderInfo(orderId);
        return ResponseEntity.ok(singleOrderInfo);
    }

	// TODO: admin 인가
	@GetMapping("/admin/order-infos")
	public ResponseEntity<OrderInfoPageResponse> adminOrderInfos(final OrderInfoSearchRequest orderInfoSearchRequest,
																 final Pageable pageable) {
		final OrderInfoPageResponse adminOrdersPagination = orderService.findAdminOrdersPagination(
				orderInfoSearchRequest, pageable);
		return ResponseEntity.ok(adminOrdersPagination);
	}
	@PostMapping("/orders")
	public ResponseEntity<OrderCreateResponseDto> createOrder(
		@RequestBody final OrderCreateRequestDto orderCreateRequestDto) {
		return ResponseEntity.ok().body(orderService.createOrder(orderCreateRequestDto));
	}

	@DeleteMapping("/orders/{id}")
	public ResponseEntity<Void> cancelOrder(@PathVariable final Long id) {
		orderService.cancelOrder(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

    private void validatePageElementSize(final Pageable pageable) {
        if (pageable.getPageSize() > MAX_PAGE_ELEMENT_REQUEST_SIZE) {
            throw new ConflictException(MAX_PAGE_ELEMENT_REQUEST_SIZE_EXCEPTION, MAX_PAGE_ELEMENT_REQUEST_SIZE);
        }
    }

}
