package com.gabia.bshop.util;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.gabia.bshop.entity.enumtype.MemberRole;
import com.gabia.bshop.security.MemberPayload;

class MemberPayloadSupportTest {

	@Test
	void MemberPayLoad_Null값이_들어오면_null을_반환한다() {
		// given
		final MemberPayload memberPayload = null;

		// when
		final Long memberId = MemberPayloadSupport.getLoggedInMemberId(memberPayload);

		// then
		assertThat(memberId).isEqualTo(null);
	}

	@Test
	void MemberPayLoad값이_들어오면_memberId을_반환한다() {
		// given
		final MemberPayload memberPayload = new MemberPayload(1L, MemberRole.NORMAL);

		// when
		final Long memberId = MemberPayloadSupport.getLoggedInMemberId(memberPayload);

		// then
		assertThat(memberId).isEqualTo(1L);
	}
}
