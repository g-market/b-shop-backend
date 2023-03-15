package com.gabia.bshop.util;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gabia.bshop.entity.enumtype.MemberRole;
import com.gabia.bshop.security.MemberPayload;

class MemberPayloadSupportTest {

	@Test
	@DisplayName("MemberPayLoad null값이 들어오면 null을 반환한다")
	void given_null_when_getLoggedInMemberId_then_return_null() {
		// given
		final Optional<MemberPayload> memberPayload = Optional.empty();

		// when
		final Long memberId = MemberPayloadSupport.getLoggedInMemberId(memberPayload);

		// then
		assertThat(memberId).isNull();
	}

	@Test
	@DisplayName("MemberPayLoad값이 들어오면 memberId를 반환한다")
	void given_member_payload_when_getLoggedInMemberId_then_return_memberId() {
		// given
		final Optional<MemberPayload> memberPayload = Optional.of(new MemberPayload(1L, MemberRole.NORMAL));

		// when
		final Long memberId = MemberPayloadSupport.getLoggedInMemberId(memberPayload);

		// then
		assertThat(memberId).isEqualTo(1L);
	}
}
