package com.gabia.bshop.controller;

import static com.gabia.bshop.exception.ErrorCode.*;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gabia.bshop.dto.request.OrderCreateRequestDto;
import com.gabia.bshop.dto.request.OrderInfoSearchRequest;
import com.gabia.bshop.dto.request.OrderUpdateStatusRequest;
import com.gabia.bshop.dto.response.OrderCreateResponseDto;
import com.gabia.bshop.dto.response.OrderInfoPageResponse;
import com.gabia.bshop.dto.response.OrderInfoSingleResponse;
import com.gabia.bshop.dto.response.OrderUpdateStatusResponse;
import com.gabia.bshop.exception.ConflictException;
import com.gabia.bshop.security.CurrentMember;
import com.gabia.bshop.security.Login;
import com.gabia.bshop.security.MemberPayload;
import com.gabia.bshop.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OrderController {

	private static final int MAX_PAGE_ELEMENT_REQUEST_SIZE = 100;
	private final OrderService orderService;

	@Login
	@GetMapping("/order-infos")
	public ResponseEntity<OrderInfoPageResponse> findOrders(@CurrentMember final MemberPayload memberPayload,
		final Pageable pageable) {
		validatePageElementSize(pageable);
		return ResponseEntity.ok(orderService.findOrdersPagination(memberPayload.id(), pageable));
	}

	@Login
	@GetMapping("/order-infos/{orderId}")
	public ResponseEntity<OrderInfoSingleResponse> singleOrderInfo(@CurrentMember final MemberPayload memberPayload,
		@PathVariable("orderId") final Long orderId) {
		final OrderInfoSingleResponse singleOrderInfo = orderService.findSingleOrderInfo(memberPayload, orderId);
		return ResponseEntity.ok(singleOrderInfo);
	}

	@Login(admin = true)
	@GetMapping("/admin/order-infos")
	public ResponseEntity<OrderInfoPageResponse> adminOrderInfos(final OrderInfoSearchRequest orderInfoSearchRequest,
		final Pageable pageable) {
		final OrderInfoPageResponse adminOrdersPagination = orderService.findAdminOrdersPagination(
			orderInfoSearchRequest, pageable);
		return ResponseEntity.ok(adminOrdersPagination);
	}

	@Login
	@PostMapping("/orders")
	public ResponseEntity<OrderCreateResponseDto> createOrder(
		@CurrentMember final MemberPayload memberPayload,
		@RequestBody @Valid final OrderCreateRequestDto orderCreateRequestDto) {
		return ResponseEntity.ok().body(orderService.createOrder(memberPayload.id(), orderCreateRequestDto));
	}

	@Login
	@DeleteMapping("/orders/{orderId}")
	public ResponseEntity<Void> cancelOrder(@CurrentMember final MemberPayload memberPayload,
		@PathVariable final Long orderId) {
		orderService.cancelOrder(memberPayload.id(), orderId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Login(admin = true)
	@PatchMapping("/orders/{orderId}")
	public ResponseEntity<OrderUpdateStatusResponse> updateOrderStatus(
		@RequestBody @Valid final OrderUpdateStatusRequest orderUpdateStatusRequest) {
		return ResponseEntity.ok().body(orderService.updateOrderStatus(orderUpdateStatusRequest));
	}

	private void validatePageElementSize(final Pageable pageable) {
		if (pageable.getPageSize() > MAX_PAGE_ELEMENT_REQUEST_SIZE) {
			throw new ConflictException(MAX_PAGE_ELEMENT_REQUEST_SIZE_EXCEPTION, MAX_PAGE_ELEMENT_REQUEST_SIZE);
		}
	}

}
