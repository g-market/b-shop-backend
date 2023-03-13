package com.gabia.bshop.service;

import static com.gabia.bshop.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gabia.bshop.dto.request.MemberUpdateRequest;
import com.gabia.bshop.dto.response.MemberResponse;
import com.gabia.bshop.entity.Member;
import com.gabia.bshop.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	private static final String MINIO_PREFIX = "http://localhost:9000/images";
	private static final String DELIMITER = "/";

	@InjectMocks
	private MemberService memberService;
	@Mock
	private MemberRepository memberRepository;

	@Test
	@DisplayName("로그인된 회원 아이디를 통해 로그인 사용자 정보를 반환한다")
	void given_savedMember_when_findLoggedInMember_then_return_memberResponse() {
		// given
		final Member jaime = JAIME.getInstance(1L);
		given(memberRepository.findById(1L))
			.willReturn(Optional.of(JAIME.getInstance(1L)));

		// when
		final MemberResponse memberResponse = memberService.findLoggedInMember(1L);

		// then
		assertAll(
			() -> verify(memberRepository).findById(1L),
			() -> assertThat(memberResponse)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("email", jaime.getEmail())
				.hasFieldOrPropertyWithValue("phoneNumber", jaime.getPhoneNumber())
				.hasFieldOrPropertyWithValue("name", jaime.getName())
				.hasFieldOrPropertyWithValue("role", jaime.getRole())
				.hasFieldOrPropertyWithValue("grade", jaime.getGrade())
				.hasFieldOrProperty("profileImageUrl")
		);
	}

	@Test
	@DisplayName("회원정보를 휴대전화 번호와 프로필 이미지를 업데이트 한다")
	void given_phoneNumberAndProfileImageUrl_when_updateLoggedInMember_then_return_memberResponse() {
		// given
		final Member jaime = JAIME.getInstance(1L);
		given(memberRepository.findById(1L)).willReturn(Optional.of(jaime));

		// when
		final MemberResponse memberResponse = memberService.updateLoggedInMember(1L,
			new MemberUpdateRequest("01012341234", MINIO_PREFIX + DELIMITER + "default-profile-image1.png"));

		// then
		assertAll(
			() -> verify(memberRepository).findById(1L),
			() -> assertThat(jaime.getPhoneNumber()).isEqualTo("01012341234"),
			() -> assertThat(jaime.getProfileImageUrl()).isEqualTo("default-profile-image1.png"),
			() -> assertThat(memberResponse)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("email", jaime.getEmail())
				.hasFieldOrPropertyWithValue("phoneNumber", "01012341234")
				.hasFieldOrPropertyWithValue("name", jaime.getName())
				.hasFieldOrPropertyWithValue("role", jaime.getRole())
				.hasFieldOrPropertyWithValue("grade", jaime.getGrade())
				.hasFieldOrProperty("profileImageUrl")
		);
	}
}
