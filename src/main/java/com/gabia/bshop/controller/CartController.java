package com.gabia.bshop.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabia.bshop.dto.CartDto;
import com.gabia.bshop.dto.request.CartCreateRequest;
import com.gabia.bshop.dto.request.CartDeleteRequest;
import com.gabia.bshop.dto.response.CartResponse;
import com.gabia.bshop.mapper.CartReqeuestMapper;
import com.gabia.bshop.security.CurrentMember;
import com.gabia.bshop.security.Login;
import com.gabia.bshop.security.MemberPayload;
import com.gabia.bshop.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {

	private final CartService cartService;

	@GetMapping
	@Login
	public ResponseEntity<List<CartResponse>> findCartList(@CurrentMember MemberPayload memberPayload) {
		final List<CartResponse> responseList = cartService.findCartList(memberPayload.id());
		return ResponseEntity.ok(responseList);
	}

	@PostMapping
	@Login
	public ResponseEntity<Void> createCart(@CurrentMember MemberPayload memberPayload,
		@RequestBody @Valid CartCreateRequest cartCreateRequest) {
		final CartDto cartDto = CartReqeuestMapper.INSTANCE.toCartDto(cartCreateRequest);
		cartService.createCart(memberPayload.id(), cartDto);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping
	@Login
	public ResponseEntity<Void> deleteCart(@CurrentMember MemberPayload memberPayload,
		@RequestBody @Valid CartDeleteRequest cartDeleteRequest) {
		final CartDto cartDto = CartReqeuestMapper.INSTANCE.toCartDto(cartDeleteRequest);
		cartService.deleteCart(memberPayload.id(), cartDto);
		return ResponseEntity.noContent().build();
	}
}
