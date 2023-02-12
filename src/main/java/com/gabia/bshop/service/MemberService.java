package com.gabia.bshop.service;

import java.text.MessageFormat;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.request.MemberUpdateRequest;
import com.gabia.bshop.dto.response.LoggedInMemberResponse;
import com.gabia.bshop.entity.Member;
import com.gabia.bshop.mapper.LoggedInMemberResponseMapper;
import com.gabia.bshop.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
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
	public void updateMember(final Long memberId, MemberUpdateRequest memberUpdateRequest) {
		final Member member = findMember(memberId);
		member.updatePhoneNumber(memberUpdateRequest.phoneNumber());
	}

	private Member findMember(final Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(
				MessageFormat.format("memberId: {0}로 등록된 사용자가 존재하지 않습니다.", memberId)));
	}

	private boolean isNotLoggedIn(final Long loggedInId) {
		return loggedInId == null;
	}
}
