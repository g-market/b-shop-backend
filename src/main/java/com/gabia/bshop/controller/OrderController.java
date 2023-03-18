package com.gabia.bshop.controller;

import org.springframework.data.domain.Page;
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
import com.gabia.bshop.dto.request.OrderUpdateStatusRequest;
import com.gabia.bshop.dto.response.OrderCreateResponse;
import com.gabia.bshop.dto.response.OrderInfoPageResponse;
import com.gabia.bshop.dto.response.OrderInfoResponse;
import com.gabia.bshop.dto.response.OrderUpdateStatusResponse;
import com.gabia.bshop.dto.searchConditions.OrderSearchConditions;
import com.gabia.bshop.security.CurrentMember;
import com.gabia.bshop.security.Login;
import com.gabia.bshop.security.MemberPayload;
import com.gabia.bshop.service.OrderService;
import com.gabia.bshop.util.validator.LimitedSizePagination;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class OrderController {

	private final OrderService orderService;

	@Login
	@GetMapping("/orders")
	public ResponseEntity<Page<OrderInfoPageResponse>> findOrderInfoList(
		@CurrentMember final MemberPayload memberPayload,
		@LimitedSizePagination final Pageable pageable, final OrderSearchConditions orderSearchConditions) {
		return ResponseEntity.ok(orderService.findOrderInfoList(pageable, memberPayload.id(), orderSearchConditions));
	}

	@Login
	@GetMapping("/orders/{orderId}")
	public ResponseEntity<OrderInfoResponse> findOrderInfo(@CurrentMember final MemberPayload memberPayload,
		@PathVariable("orderId") final Long orderId) {
		return ResponseEntity.ok(orderService.findOrderInfo(memberPayload, orderId));
	}

	@Login(admin = true)
	@GetMapping("/admin/orders")
	public ResponseEntity<Page<OrderInfoPageResponse>> findAllOrderInfoList(
		@LimitedSizePagination final Pageable pageable, final OrderSearchConditions orderSearchConditions) {
		return ResponseEntity.ok(orderService.findAllOrderInfoList(orderSearchConditions, pageable));
	}

	@Login
	@PostMapping("/orders")
	public ResponseEntity<OrderCreateResponse> createOrder(
		@CurrentMember final MemberPayload memberPayload,
		@RequestBody @Valid final OrderCreateRequest orderCreateRequest) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(orderService.createOrder(memberPayload.id(), orderCreateRequest));
	}

	@Login
	@DeleteMapping("/orders/{orderId}")
	public ResponseEntity<Void> cancelOrder(@CurrentMember final MemberPayload memberPayload,
		@PathVariable final Long orderId) {
		orderService.cancelOrder(memberPayload.id(), orderId);
		return ResponseEntity.noContent().build();
	}

	@Login(admin = true)
	@PatchMapping("/orders")
	public ResponseEntity<OrderUpdateStatusResponse> updateOrderStatus(
		@RequestBody @Valid final OrderUpdateStatusRequest orderUpdateStatusRequest) {
		return ResponseEntity.ok(orderService.updateOrderStatus(orderUpdateStatusRequest));
	}
}
