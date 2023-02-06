package com.gabia.bshop.controller;

import com.gabia.bshop.security.CurrentMember;
import com.gabia.bshop.dto.request.MemberUpdateRequest;
import com.gabia.bshop.dto.response.LoggedInMemberResponse;
import com.gabia.bshop.security.Login;
import com.gabia.bshop.security.MemberPayload;
import com.gabia.bshop.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    @Login
    public LoggedInMemberResponse showLoggedIn(@CurrentMember final MemberPayload memberPayload) {
        return memberService.findLoggedInMember(memberPayload.id());
    }

    @PatchMapping("/me")
    @Login
    public ResponseEntity<Void> updateMe(@CurrentMember final MemberPayload memberPayload,
            @Valid @RequestBody final MemberUpdateRequest memberUpdateRequest) {
        memberService.updateMember(memberPayload.id(), memberUpdateRequest);
        return ResponseEntity.ok().build();
    }

}
