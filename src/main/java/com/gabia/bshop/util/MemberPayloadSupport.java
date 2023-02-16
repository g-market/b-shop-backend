package com.gabia.bshop.util;

import java.util.Optional;

import com.gabia.bshop.security.MemberPayload;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberPayloadSupport {

	public static Long getLoggedInMemberId(final Optional<MemberPayload> memberPayload) {
		return memberPayload.map(MemberPayload::id).orElse(null);
	}
}
