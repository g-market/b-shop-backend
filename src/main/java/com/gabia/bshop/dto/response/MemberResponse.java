package com.gabia.bshop.dto.response;

import com.gabia.bshop.entity.enumtype.MemberGrade;
import com.gabia.bshop.entity.enumtype.MemberRole;

public record MemberResponse(
	Long id,
	String email,
	String phoneNumber,
	String name,
	MemberRole role,
	MemberGrade grade,
	String profileImageUrl
) {
}
