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
import com.gabia.bshop.mapper.CartRequestMapper;
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
		final CartDto cartDto = CartRequestMapper.INSTANCE.toCartDto(cartCreateRequest);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(cartService.createCart(memberPayload.id(), cartDto));
	}

	@Login
	@DeleteMapping("/carts")
	public ResponseEntity<Void> deleteCart(@CurrentMember MemberPayload memberPayload,
		@RequestBody @Valid CartDeleteRequest cartDeleteRequest) {
		final CartDto cartDto = CartRequestMapper.INSTANCE.toCartDto(cartDeleteRequest);
		cartService.deleteCart(memberPayload.id(), cartDto);
		return ResponseEntity.noContent().build();
	}
}
