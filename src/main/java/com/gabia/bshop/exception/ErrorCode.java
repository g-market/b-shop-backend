package com.gabia.bshop.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
	ITEM_OPTION_NOT_FOUND_EXCEPTION(NOT_FOUND, "optionId: {0}는 존재하지 않는 아이템 옵션 입니다."),
	CATEGORY_NOT_FOUND_EXCEPTION(NOT_FOUND, "categoryId: {0}는 존재하지 않는 카테고리 입니다."),
	IMAGE_NOT_FOUND_EXCEPTION(NOT_FOUND, "imageId: {0}는 존재하지 않는 이미지 입니다."),
	GRADE_NOT_FOUND_EXCEPTION(NOT_FOUND, "gradeId: {0}는 존재하지 않는 회원등급 입니다."),
	ITEMOPTION_NOT_FOUND_EXCEPTION(NOT_FOUND, "유효하지 않은 상품이 존재합니다."),

	// 409(Conflict)
	ITEM_OPTION_OUT_OF_STOCK_EXCEPTION(CONFLICT, "상품의 재고가 부족합니다.(현재 재고 수량은 {0}개 입니다.)"),
	ORDER_STATUS_ALREADY_COMPLETED_EXCEPTION(CONFLICT, "상품의 상태가 완료된 상태입니다."),
	ORDER_STATUS_ALREADY_CANCELLED_EXCEPTION(CONFLICT, "상품의 상태가 취소된 상태입니다."),
	MAX_PAGE_ELEMENT_REQUEST_SIZE_EXCEPTION(CONFLICT, "한 페이지의 최대 {0}개까지 조회가 가능합니다."),
	MAX_FILE_UPLOAD_REQUEST_EXCEPTION(CONFLICT, "한번에 최대 {0}개의 파일만 업로드 가능합니다"),
	ITEM_STATUS_NOT_PUBLIC_EXCEPTION(CONFLICT, "현재 판매하지 않는 상품이 존재합니다."),

	// 500(Internal Server Error)
	OAUTH_PROCESSING_EXCEPTION(INTERNAL_SERVER_ERROR, "Oauth 진행 중 예상치 못한 문제가 생겼습니다."),
	OAUTH_JSON_PARSING_EXCEPTION(INTERNAL_SERVER_ERROR, "Oauth 진행 중 데이터 파싱에 실패했습니다."),
	MINIO_UPLOAD_EXCEPTION(INTERNAL_SERVER_ERROR, "데이터 업로드 과정 중 문제가 발생했습니다."),

	// 503(Service Temporarily Unavailable)
	HIWORKS_SERVER_ERROR_EXCEPTION(SERVICE_UNAVAILABLE, "하이웍스 서버에 문제가 있습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
