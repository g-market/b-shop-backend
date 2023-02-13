package com.gabia.bshop.exception;

import java.net.BindException;
import java.text.MessageFormat;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.util.WebUtils;

import com.gabia.bshop.security.provider.RefreshTokenCookieProvider;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final String REFRESH_TOKEN = "refreshToken";
	private static final String REQUEST_DUPLICATED_MESSAGE = "요청이 중복될 수 없습니다";
	private static final String REQUEST_DATA_FORMAT_ERROR_MESSAGE = "요청으로 넘어온 값의 HTTP Body 형식에 맞지 않습니다.";
	private static final String INTERNAL_SERVER_ERROR_MESSAGE_FORMAT = "서버 오류가 발생했습니다. : \n\n {}";
	private static final String LOG_FORMAT = "Class : {}, Message : {}";

	private final RefreshTokenCookieProvider refreshTokenCookieProvider;

	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<ExceptionResponse> handleApplicationException(final ApplicationException e) {
		log.info(LOG_FORMAT, e.getClass().getSimpleName(), e.getExceptionResponse().message());
		return ResponseEntity.status(e.getHttpStatus())
			.body(e.getExceptionResponse());
	}

	@ExceptionHandler(UnAuthorizedException.class)
	public ResponseEntity<ExceptionResponse> handleRefreshTokenException(final UnAuthorizedException e,
		final HttpServletRequest request) {
		log.info(LOG_FORMAT, e.getClass().getSimpleName(), e.getExceptionResponse().message());
		final ExceptionResponse responseBody = new ExceptionResponse(e.getExceptionResponse().message());
		final Cookie cookie = WebUtils.getCookie(request, REFRESH_TOKEN);
		if (cookie != null) {
			final ResponseCookie responseCookie = refreshTokenCookieProvider.createLogoutCookie();
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
				.body(responseBody);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body(responseBody);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(final EntityNotFoundException e) {
		log.info(LOG_FORMAT, e.getClass().getSimpleName(), e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(new ExceptionResponse(e.getMessage()));
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ExceptionResponse> handleInvalidValueException(final DataIntegrityViolationException e) {
		log.info(LOG_FORMAT, e.getClass().getSimpleName(), e.getMessage());

		return ResponseEntity.badRequest()
			.body(new ExceptionResponse(REQUEST_DUPLICATED_MESSAGE));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException(
		final HttpMessageNotReadableException e) {
		log.info(LOG_FORMAT, e.getClass().getSimpleName(), e.getMessage());
		return ResponseEntity.badRequest()
			.body(new ExceptionResponse(REQUEST_DATA_FORMAT_ERROR_MESSAGE));
	}

	@ExceptionHandler({BindException.class, MethodArgumentTypeMismatchException.class})
	public ResponseEntity<ExceptionResponse> handleInvalidQueryParameterException(Exception e) {
		log.info(LOG_FORMAT, e.getClass().getSimpleName(), e.getMessage());
		return ResponseEntity.badRequest()
			.body(new ExceptionResponse(REQUEST_DATA_FORMAT_ERROR_MESSAGE));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> handleValidationException(final MethodArgumentNotValidException e) {
		log.info(LOG_FORMAT, e.getClass().getSimpleName(), e.getMessage());
		final StringBuilder stringBuilder = new StringBuilder();
		e.getBindingResult().getAllErrors().forEach((error) -> stringBuilder.append(error.getDefaultMessage())
			.append(System.lineSeparator()));
		return ResponseEntity.badRequest()
			.body(new ExceptionResponse(stringBuilder.toString()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleUnhandledException(final Exception e) {
		log.warn(LOG_FORMAT, e.getClass().getSimpleName(), e.getMessage());
		return ResponseEntity.internalServerError()
			.body(new ExceptionResponse(MessageFormat.format(INTERNAL_SERVER_ERROR_MESSAGE_FORMAT, e.getMessage())));
	}
}
