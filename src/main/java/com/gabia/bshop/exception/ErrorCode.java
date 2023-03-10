package com.gabia.bshop.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// 400(Bad request)
	INCORRECT_URL_EXCEPTION(BAD_REQUEST, "유효하지 않은 URL 입니다"),
	NOT_ACCEPTED_FILE_FORMAT_EXCEPTION(BAD_REQUEST, "유효하지 않는 파일 형식 입니다."),
	NO_FILE_EXCEPTION(BAD_REQUEST, "지정된 파일이 없습니다."),
	INVALID_ITEM_OPTION_NOT_FOUND_EXCEPTION(BAD_REQUEST, "유효하지 않은 상품이 존재합니다."),

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
	ITEM_OPTION_NOT_FOUND_EXCEPTION(NOT_FOUND, "itemId: {0}의 optionId: {1} 를 찾을 수 없습니다."),
	CATEGORY_NOT_FOUND_EXCEPTION(NOT_FOUND, "categoryId: {0}는 존재하지 않는 카테고리 입니다."),
	IMAGE_NOT_FOUND_EXCEPTION(NOT_FOUND, "imageId: {0}는 존재하지 않는 이미지 입니다."),
	ITEM_IMAGE_NOT_FOUND_EXCEPTION(NOT_FOUND, "itemId: {0}의 imageId: {1} 를 찾을 수 없습니다."),
	GRADE_NOT_FOUND_EXCEPTION(NOT_FOUND, "gradeId: {0}는 존재하지 않는 회원등급 입니다."),
	ITEM_RESERVATION_NOT_FOUND_EXCEPTION(NOT_FOUND, "itemId : {0}에 대한 상품예약을 찾을 수 없습니다."),

	// 409(Conflict)
	ITEM_OPTION_OUT_OF_STOCK_EXCEPTION(CONFLICT, "itemOptionId: {0} 상품의 재고가 부족합니다.(현재 재고:{1})"),
	ORDER_STATUS_ALREADY_CANCELLED_EXCEPTION(CONFLICT, "상품의 상태가 이미 취소된 상태입니다."),
	ORDER_STATUS_ALREADY_UPDATED_EXCEPTION(CONFLICT, "상품의 상태가 이미 {0} 상태입니다."),
	MAX_PAGE_ELEMENT_REQUEST_SIZE_EXCEPTION(CONFLICT, "한 페이지의 최대 {0}개까지 조회가 가능합니다."),
	MAX_FILE_UPLOAD_REQUEST_EXCEPTION(CONFLICT, "한번에 최대 {0}개의 파일만 업로드 가능합니다"),
	ITEM_STATUS_NOT_PUBLIC_EXCEPTION(CONFLICT, "현재 판매하지 않는 상품이 존재합니다."),
	RESERVATION_TIME_NOT_VALID_EXCEPTION(CONFLICT, "예약시간: {0} 은 현재시간 이후의 시간이어야 합니다."),
	CATEGORY_ITEM_DELETE_EXCEPTION(CONFLICT, "categoryId: {0}인 상품이 존재합니다."),
	MAX_ITEM_OPTION_LIMITATION_EXCEPTION(CONFLICT, "최대로 등록할 수 있는 상품 옵션의 수는 {0}개 입니다."),
	MAX_ITEM_IMAGE_LIMITATION_EXCEPTION(CONFLICT, "최대로 등록할 수 있는 상품 이미지의 수는 {0}개 입니다."),

	// 500(Internal Server Error)
	OAUTH_PROCESSING_EXCEPTION(INTERNAL_SERVER_ERROR, "Oauth 진행 중 예상치 못한 문제가 생겼습니다."),
	OAUTH_JSON_PARSING_EXCEPTION(INTERNAL_SERVER_ERROR, "Oauth 진행 중 데이터 파싱에 실패했습니다."),
	MINIO_UPLOAD_EXCEPTION(INTERNAL_SERVER_ERROR, "데이터 업로드 과정 중 문제가 발생했습니다."),
	MINIO_EXCEPTION(INTERNAL_SERVER_ERROR, "데이터 검색 과정에서 문제가 발생했습니다"),
	REDIS_JSON_PARSING_EXCEPTION(INTERNAL_SERVER_ERROR, "Redis Value의 데이터 파싱에 실패했습니다."),

	// 503(Service Temporarily Unavailable)
	HIWORKS_SERVER_ERROR_EXCEPTION(SERVICE_UNAVAILABLE, "하이웍스 서버에 문제가 있습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
