package com.gabia.bshop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabia.bshop.dto.request.MemberUpdateRequest;
import com.gabia.bshop.dto.response.MemberResponse;
import com.gabia.bshop.security.CurrentMember;
import com.gabia.bshop.security.Login;
import com.gabia.bshop.security.MemberPayload;
import com.gabia.bshop.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/me")
	@Login
	public MemberResponse findLoggedInMember(@CurrentMember final MemberPayload memberPayload) {
		return memberService.findLoggedInMember(memberPayload.id());
	}

	@PatchMapping("/me")
	@Login
	public ResponseEntity<Void> updateLoggedInMember(@CurrentMember final MemberPayload memberPayload,
		@Valid @RequestBody final MemberUpdateRequest memberUpdateRequest) {
		memberService.updateLoggedInMember(memberPayload.id(), memberUpdateRequest);
		return ResponseEntity.ok().build();
	}
}
