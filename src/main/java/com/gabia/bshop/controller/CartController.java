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

	@PostMapping
	@Login
	public ResponseEntity<Void> save(@CurrentMember MemberPayload memberPayload,
		@RequestBody @Valid CartCreateRequest cartCreateRequest) {
		final CartDto cartDto = CartReqeuestMapper.INSTANCE.toCartDto(cartCreateRequest);
		cartService.save(memberPayload.id(), cartDto);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	@Login
	public ResponseEntity<List<CartResponse>> findAll(@CurrentMember MemberPayload memberPayload) {
		final List<CartResponse> responseList = cartService.findAll(memberPayload.id());
		return ResponseEntity.ok(responseList);
	}

	@DeleteMapping
	@Login
	public ResponseEntity<Void> delete(@CurrentMember MemberPayload memberPayload,
		@RequestBody @Valid CartDeleteRequest cartDeleteRequest) {
		final CartDto cartDto = CartReqeuestMapper.INSTANCE.toCartDto(cartDeleteRequest);
		cartService.delete(memberPayload.id(), cartDto);
		return ResponseEntity.noContent().build();
	}
}
