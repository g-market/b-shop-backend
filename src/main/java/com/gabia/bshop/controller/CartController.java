package com.gabia.bshop.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gabia.bshop.dto.CartDto;
import com.gabia.bshop.dto.request.CartCreateRequest;
import com.gabia.bshop.dto.request.CartDeleteRequest;
import com.gabia.bshop.dto.response.CartResponse;
import com.gabia.bshop.mapper.CartMapper;
import com.gabia.bshop.security.CurrentMember;
import com.gabia.bshop.security.Login;
import com.gabia.bshop.security.MemberPayload;
import com.gabia.bshop.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class CartController {

	private final CartService cartService;

	@Login
	@GetMapping("/carts")
	public ResponseEntity<List<CartResponse>> findCartList(@CurrentMember MemberPayload memberPayload) {
		return ResponseEntity.ok(cartService.findCartList(memberPayload.id()));
	}

	@Login
	@PostMapping("/carts")
	public ResponseEntity<CartDto> createCart(@CurrentMember MemberPayload memberPayload,
		@RequestBody @Valid CartCreateRequest cartCreateRequest) {
		final CartDto cartDto = CartMapper.INSTANCE.cartCreateRequestToCartDto(cartCreateRequest);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(cartService.createCart(memberPayload.id(), cartDto));
	}

	/**
	 * 장바구니 단건 삭제
	 */
	@Login
	@DeleteMapping("/cart")
	public ResponseEntity<Void> deleteCart(@CurrentMember MemberPayload memberPayload,
		@RequestBody @Valid CartDeleteRequest cartDeleteRequest) {
		final CartDto cartDto = CartMapper.INSTANCE.cartDeleteRequestToCartDto(cartDeleteRequest);
		cartService.deleteCart(memberPayload.id(), cartDto);
		return ResponseEntity.noContent().build();
	}

	/**
	 * 사용된 장바구니 전체 삭제하는 메서드
	 */
	@Login
	@DeleteMapping("/carts")
	public ResponseEntity<Void> deleteCartList(@CurrentMember MemberPayload memberPayload,
		@RequestBody @Valid List<CartDeleteRequest> cartDeleteRequestList) {
		final List<CartDto> cartDtoList = cartDeleteRequestList.stream()
			.map(CartMapper.INSTANCE::cartDeleteRequestToCartDto)
			.toList();
		cartService.deleteCartList(memberPayload.id(), cartDtoList);
		return ResponseEntity.noContent().build();
	}
}
