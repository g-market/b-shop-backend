package com.gabia.bshop.service;

import static com.gabia.bshop.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.request.MemberUpdateRequest;
import com.gabia.bshop.dto.response.LoggedInMemberResponse;
import com.gabia.bshop.entity.Member;
import com.gabia.bshop.exception.NotFoundException;
import com.gabia.bshop.mapper.LoggedInMemberResponseMapper;
import com.gabia.bshop.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	public LoggedInMemberResponse findLoggedInMember(final Long loggedInId) {
		final Member member = findMember(loggedInId);
		return LoggedInMemberResponseMapper.INSTANCE.from(member);
	}

	@Transactional
	public void updateLoggedInMember(final Long memberId, MemberUpdateRequest memberUpdateRequest) {
		final Member member = findMember(memberId);
		member.updatePhoneNumber(memberUpdateRequest.phoneNumber());
	}

	private Member findMember(final Long memberId) {
		return findMemberById(memberId);
	}

	private boolean isNotLoggedIn(final Long loggedInId) {
		return loggedInId == null;
	}

	private Member findMemberById(final Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND_EXCEPTION, memberId));
	}
}
