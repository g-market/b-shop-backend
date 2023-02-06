package com.gabia.bshop.controller;

import static com.gabia.bshop.security.provider.RefreshTokenCookieProvider.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.gabia.bshop.dto.response.AccessTokenResponse;
import com.gabia.bshop.dto.response.AdminLoginResponse;
import com.gabia.bshop.dto.response.IssuedTokensResponse;
import com.gabia.bshop.dto.response.LoginResponse;
import com.gabia.bshop.dto.response.LoginResult;
import com.gabia.bshop.exception.UnAuthorizedException;
import com.gabia.bshop.mapper.LoginResponseMapper;
import com.gabia.bshop.security.provider.RefreshTokenCookieProvider;
import com.gabia.bshop.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

	private final AuthService authService;

	private final RefreshTokenCookieProvider refreshTokenCookieProvider;

	@Value("${hiworks.client.id}")
	private String clientId;

	@GetMapping("/oauth2/authorization/hiworks")
	public ResponseEntity<String> getOauthUrl() {
		final String loginPage = getLoginUrl();
		return ResponseEntity.ok(loginPage);
	}

	@GetMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestParam("auth_code") final String authCode) {
		final LoginResult loginResult = authService.login(authCode);
		final String refreshToken = loginResult.refreshToken();
		ResponseCookie cookie = refreshTokenCookieProvider.createCookie(refreshToken);
		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, cookie.toString())
			.body(LoginResponseMapper.INSTANCE.from(loginResult));
	}

	@GetMapping("/login/admin")
	public AdminLoginResponse loginAdmin(@RequestParam("authCode") final String authCode) {
		return authService.loginAdmin(authCode);
	}

	@GetMapping("/logout")
	public ResponseEntity<Void> logout(
		@CookieValue(value = REFRESH_TOKEN, required = false) final String refreshToken) {
		validateRefreshTokenExists(refreshToken);
		return ResponseEntity.noContent()
			.header(HttpHeaders.SET_COOKIE,
				refreshTokenCookieProvider.createLogoutCookie().toString())
			.build();
	}

	@PostMapping("/accessToken")
	public ResponseEntity<AccessTokenResponse> issueAccessToken(
		@CookieValue(value = REFRESH_TOKEN, required = false) final String refreshToken) {
		validateRefreshTokenExists(refreshToken);
		final IssuedTokensResponse issuedTokensResponse = authService.issueAccessToken(
			refreshToken);
		final ResponseCookie responseCookie = refreshTokenCookieProvider.createCookie(
			issuedTokensResponse.refreshToken());
		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
			.body(new AccessTokenResponse(issuedTokensResponse.accessToken()));
	}

	private String getLoginUrl() {
		return UriComponentsBuilder
			.fromUriString("https://api.hiworks.com/api/v2/oauth2/auth")
			.queryParam("client_id", clientId)
			.queryParam("access_type", "offline")
			.toUriString();
	}

	private void validateRefreshTokenExists(final String refreshToken) {
		if (refreshToken == null) {
			throw new UnAuthorizedException("리프레시 토큰이 만료됐습니다.");
		}
	}
}
