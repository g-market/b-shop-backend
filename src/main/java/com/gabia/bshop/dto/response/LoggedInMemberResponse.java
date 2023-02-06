package com.gabia.bshop.dto.response;

/**
 * <h1>MemberController$프로필을 조회 DTO</h1>
 * @see com.gabia.bshop.controller.MemberController
 * @author jaime
 */
public record LoggedInMemberResponse(
	Long id,
	String email,
	String phoneNumber,
	String name,
	String hiworksId
) {

}
