package com.gabia.bshop.dto.response;

/**
 * <h1>로그인된 사용자 프로필을 조회 DTO</h1>
 * @see com.gabia.bshop.controller.MemberController
 * @author jaime
 */
public record LoginMemberResponse(
	Long id,
	String hiworksId,
	String name
) {

}
