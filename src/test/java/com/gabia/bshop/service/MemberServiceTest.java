package com.gabia.bshop.service;

import static com.gabia.bshop.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gabia.bshop.dto.request.MemberUpdateRequest;
import com.gabia.bshop.dto.response.LoggedInMemberResponse;
import com.gabia.bshop.entity.Member;
import com.gabia.bshop.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@InjectMocks
	private MemberService memberService;

	@Mock
	private MemberRepository memberRepository;

	@Test
	void 로그인된_회원_아이디를_통해_로그인_사용자_정보를_반환한다() {
		// given
		final Member jaime = JAIME.getInstance(1L);
		given(memberRepository.findById(1L))
			.willReturn(Optional.of(JAIME.getInstance(1L)));

		// when
		final LoggedInMemberResponse loggedInMemberResponse = memberService.findLoggedInMember(1L);

		// then
		assertAll(
			() -> verify(memberRepository).findById(1L),
			() -> assertThat(loggedInMemberResponse)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("email", jaime.getEmail())
				.hasFieldOrPropertyWithValue("phoneNumber", jaime.getPhoneNumber())
				.hasFieldOrPropertyWithValue("name", jaime.getName())
				.hasFieldOrPropertyWithValue("hiworksId", jaime.getHiworksId())
		);
	}

	@Test
	void 회원정보를_업데이트_한다() {
		// given
		final Member jaime = JAIME.getInstance(1L);
		given(memberRepository.findById(1L))
			.willReturn(Optional.of(jaime));

		// when
		memberService.updateMember(1L, new MemberUpdateRequest("01012341234"));

		// then
		assertAll(
			() -> verify(memberRepository).findById(1L),
			() -> assertThat(jaime.getPhoneNumber()).isEqualTo("01012341234")
		);
	}
}
