package com.gabia.bshop.controller;

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

import com.gabia.bshop.dto.request.OrderCreateRequest;
import com.gabia.bshop.dto.request.OrderInfoSearchRequest;
import com.gabia.bshop.dto.request.OrderUpdateStatusRequest;
import com.gabia.bshop.dto.response.OrderCreateResponse;
import com.gabia.bshop.dto.response.OrderInfoPageResponse;
import com.gabia.bshop.dto.response.OrderInfoSingleResponse;
import com.gabia.bshop.dto.response.OrderUpdateStatusResponse;
import com.gabia.bshop.security.CurrentMember;
import com.gabia.bshop.security.Login;
import com.gabia.bshop.security.MemberPayload;
import com.gabia.bshop.service.CartService;
import com.gabia.bshop.service.OrderService;
import com.gabia.bshop.util.validator.LimitedSizePagination;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class OrderController {

	private final OrderService orderService;
	private final CartService cartService;

	@Login
	@GetMapping("/orders")
	public ResponseEntity<OrderInfoPageResponse> findOrderInfoList(@CurrentMember final MemberPayload memberPayload,
		@LimitedSizePagination final Pageable pageable) {
		return ResponseEntity.ok(orderService.findOrderInfoList(memberPayload.id(), pageable));
	}

	@Login
	@GetMapping("/orders/{orderId}")
	public ResponseEntity<OrderInfoSingleResponse> findOrderInfo(@CurrentMember final MemberPayload memberPayload,
		@PathVariable("orderId") final Long orderId) {
		final OrderInfoSingleResponse singleOrderInfo = orderService.findOrderInfo(memberPayload, orderId);
		return ResponseEntity.ok(singleOrderInfo);
	}

	@Login(admin = true)
	@GetMapping("/admin/orders")
	public ResponseEntity<OrderInfoPageResponse> findAllOrderInfoList(
		final OrderInfoSearchRequest orderInfoSearchRequest,
		@LimitedSizePagination final Pageable pageable) {
		final OrderInfoPageResponse adminOrdersPagination = orderService.findAllOrderInfoList(
			orderInfoSearchRequest, pageable);
		return ResponseEntity.ok(adminOrdersPagination);
	}

	@Login
	@PostMapping("/orders")
	public ResponseEntity<OrderCreateResponse> createOrder(
		@CurrentMember final MemberPayload memberPayload,
		@RequestBody @Valid final OrderCreateRequest orderCreateRequest) {
		final OrderCreateResponse orderCreateResponse = orderService.createOrder(memberPayload.id(),
			orderCreateRequest);
		cartService.deleteCartList(memberPayload.id(), orderCreateRequest.orderItemDtoList());
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(orderCreateResponse);
	}

	@Login
	@DeleteMapping("/orders/{orderId}")
	public ResponseEntity<Void> cancelOrder(@CurrentMember final MemberPayload memberPayload,
		@PathVariable final Long orderId) {
		orderService.cancelOrder(memberPayload.id(), orderId);
		return ResponseEntity.noContent().build();
	}

	@Login(admin = true)
	@PatchMapping("/orders/{orderId}")
	public ResponseEntity<OrderUpdateStatusResponse> updateOrderStatus(
		@RequestBody @Valid final OrderUpdateStatusRequest orderUpdateStatusRequest) {
		return ResponseEntity.ok(orderService.updateOrderStatus(orderUpdateStatusRequest));
	}
}
