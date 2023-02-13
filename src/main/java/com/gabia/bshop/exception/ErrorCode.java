package com.gabia.bshop.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <h1>HTTP Status Custom</h1>
 * <p>
 *     401(Unauthorized): 클라이언트는 요청된 응답을 얻기 위해 자신을 인증해야 합니다.
 * </p>
 * <p>
 *     403(Forbidden): 인증은 되었지만, 권한이 없기 때문에 서버가 요청된 리소스 제공을 거부하고 있습니다. <br/>
 *     - 즉, 사용자는 구분이 되지만, 일반 사용자가 Admin 권한 Resource를 사용할 때 이러한 에러를 사용하면 됩니다.
 * </p>
 * <p>
 *     404(NotFound)
 *     <ul>
 *         <li>서버가 요청한 리소스를 찾을 수 없는 경우</li>
 *         <li>엔드포인트가 유효하지만 리소스 자체가 존재하지 않는 경우</li>
 *         <li>권한이 없는 클라이언트로부터 리소스의 존재를 숨기는 대신 이 응답(403 대신 404로)</li>
 *     </ul>
 * </p>
 *
 * @author jaime
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// 401(Unauthorized)
	TOKEN_INVALID_FORMAT_EXCEPTION(UNAUTHORIZED, "토큰이 잘못된 형식입니다."),
	TOKEN_NOT_EXIST_EXCEPTION(UNAUTHORIZED, "토큰이 존재하지 않습니다."),
	TOKEN_EXPIRED_EXCEPTION(UNAUTHORIZED, "토큰이 만료됐습니다."),
	REFRESH_TOKEN_NOT_FOUND_EXCEPTION(UNAUTHORIZED, "서버에 존재하지 않는 리프레시 토큰입니다."),
	REFRESH_TOKEN_NOT_EXIST_EXCEPTION(UNAUTHORIZED, "리프레시 토큰이 존재하지 않습니다."),
	REFRESH_TOKEN_DUPLICATED_SAVED_EXCEPTION(UNAUTHORIZED, "예상보다 더 많은 리프레시 토큰이 서버에 반영됐습니다."),
	REFRESH_TOKEN_EXPIRED_EXCEPTION(UNAUTHORIZED, "리프레시 토큰이 만료됐습니다."),
	HIWORKS_AUTH_CODE_INVALID_EXCEPTION(UNAUTHORIZED, "잘못된 하이웍스 로그인 요청입니다."),

	// 403(Forbidden)
	NOT_ADMIN_EXCEPTION(FORBIDDEN, "관리자가 아닙니다."),

	// 404(Not Found)
	MEMBER_NOT_FOUND_EXCEPTION(NOT_FOUND, "hiworksId: {0}로 등록된 사용자가 존재하지 않습니다."),
	ORDER_NOT_FOUND_EXCEPTION(NOT_FOUND, "orderId: {0}는 존재하지 않는 주문 ID 입니다."),
	ITEM_NOT_FOUND_EXCEPTION(NOT_FOUND, "itemId: {0}는 존재하지 않는 아이템 입니다."),
	CATEGORY_NOT_FOUND_EXCEPTION(NOT_FOUND, "categoryId: {0}는 존재하지 않는 카테고리 입니다."),
	IMAGE_NOT_FOUND_EXCEPTION(NOT_FOUND, "imageId: {0}는 존재하지 않는 이미지 입니다."),
	GRADE_NOT_FOUND_EXCEPTION(NOT_FOUND, "gradeId: {0}는 존재하지 않는 회원등급 입니다."),

	// 409(Conflict)
	ITEM_OPTION_OUT_OF_STOCK_EXCEPTION(CONFLICT, "상품의 재고가 부족합니다.(현재 재고 수량은 {0}개 입니다.)"),
	ORDER_STATUS_ALREADY_COMPLETED_EXCEPTION(CONFLICT, "상품의 상태가 완료된 상태입니다."),
	ORDER_STATUS_ALREADY_CANCELLED_EXCEPTION(CONFLICT, "상품의 상태가 취소된 상태입니다."),
	MAX_PAGE_ELEMENT_REQUEST_SIZE_EXCEPTION(CONFLICT, "한 페이지의 최대 {0}개까지 조회가 가능합니다."),

	// 500(Internal Server Error)
	OAUTH_PROCESSING_EXCEPTION(INTERNAL_SERVER_ERROR, "Oauth 진행 중 예상치 못한 문제가 생겼습니다."),
	OAUTH_JSON_PARSING_EXCEPTION(INTERNAL_SERVER_ERROR, "Oauth 진행 중 데이터 파싱에 실패했습니다."),

	// 503(Service Temporarily Unavailable)
	HIWORKS_SERVER_ERROR_EXCEPTION(SERVICE_UNAVAILABLE, "하이웍스 서버에 문제가 있습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
