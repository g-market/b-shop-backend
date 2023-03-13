package com.gabia.bshop.service;

import static com.gabia.bshop.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.request.MemberUpdateRequest;
import com.gabia.bshop.dto.response.MemberResponse;
import com.gabia.bshop.entity.Member;
import com.gabia.bshop.exception.NotFoundException;
import com.gabia.bshop.mapper.MemberResponseMapper;
import com.gabia.bshop.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	public MemberResponse findLoggedInMember(final Long loggedInId) {
		final Member member = findMember(loggedInId);
		return MemberResponseMapper.INSTANCE.memberToMemberResponse(member);
	}

	@Transactional
	public MemberResponse updateLoggedInMember(final Long memberId, final MemberUpdateRequest memberUpdateRequest) {
		final Member member = findMember(memberId);
		final String profileImageUrl = memberUpdateRequest.profileImageUrl();
		final String profileImage = profileImageUrl.substring(profileImageUrl.lastIndexOf("/") + 1);

		member.updateProfile(memberUpdateRequest.phoneNumber(), profileImage);

		return MemberResponseMapper.INSTANCE.memberToMemberResponse(member);
	}

	private Member findMember(final Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND_EXCEPTION, memberId));
	}
}
