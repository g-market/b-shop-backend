package com.gabia.bshop.security.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabia.bshop.dto.response.HiworksProfileResponse;
import com.gabia.bshop.dto.response.HiworksTokenResponse;
import com.gabia.bshop.exception.ConflictException;
import com.gabia.bshop.exception.InternalServerException;

import lombok.Builder;

@Builder
public record ProdHiworksOauthClient(
	String clientId,
	String clientSecret,
	String accessTokenUrl,
	String profileUrl,
	ObjectMapper objectMapper,
	HttpClient httpClient
) implements HiworksOauthClient {

	private static final String GRANT_TYPE = "authorization_code";
	private static final String ACCESS_TYPE = "offline";
	private static final String ERROR_CODE = "ERR";

	@Override
	public String getAccessToken(final String authCode) {
		final String formData = buildBodyFormData(authCode);
		try {
			final HttpResponse<String> accessTokenResponse = requestAccessToken(formData);
			return parseAccessToken(accessTokenResponse);
		} catch (IOException e) {
			throw new InternalServerException("Oauth 진행 중 예상치 못한 문제가 생겼습니다.");
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new InternalServerException("Oauth 진행 중 예상치 못한 문제가 생겼습니다.");
		}
	}

	private String buildBodyFormData(final String authCode) {
		final Map<String, String> formData = Map.of(
			"client_id", clientId,
			"client_secret", clientSecret,
			"grant_type", GRANT_TYPE,
			"auth_code", authCode,
			"access_type", ACCESS_TYPE
		);
		return toRequestForm(formData);
	}

	private String toRequestForm(final Map<String, String> formData) {
		final StringBuilder formBodyBuilder = new StringBuilder();
		for (Map.Entry<String, String> singleEntry : formData.entrySet()) {
			if (formBodyBuilder.length() > 0) {
				formBodyBuilder.append("&");
			}
			formBodyBuilder.append(URLEncoder.encode(singleEntry.getKey(), StandardCharsets.UTF_8));
			formBodyBuilder.append("=");
			formBodyBuilder.append(URLEncoder.encode(singleEntry.getValue(), StandardCharsets.UTF_8));
		}
		return formBodyBuilder.toString();
	}

	private HttpResponse<String> requestAccessToken(final String requestBody)
		throws IOException, InterruptedException {
		final HttpRequest accessTokenRequest = buildAccessTokenRequest(requestBody);
		return httpClient.send(accessTokenRequest, BodyHandlers.ofString());
	}

	private HttpRequest buildAccessTokenRequest(final String requestBody) {
		return HttpRequest.newBuilder(toUri(accessTokenUrl))
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
			.POST(HttpRequest.BodyPublishers.ofString(requestBody))
			.build();
	}

	private URI toUri(final String accessTokenUrl) {
		try {
			return new URI(accessTokenUrl);
		} catch (URISyntaxException e) {
			throw new InternalServerException("Oauth 진행 중 예상치 못한 문제가 생겼습니다.");
		}
	}

	private String parseAccessToken(final HttpResponse<String> response) {
		validateAccessTokenSuccess(response);
		try {
			final JSONObject jsonObject = new JSONObject(response.body());
			final HiworksTokenResponse hiworksTokenResponse =
				objectMapper.readValue(jsonObject.get("data").toString(), HiworksTokenResponse.class);
			return hiworksTokenResponse.accessToken();
		} catch (JsonProcessingException e) {
			throw new InternalServerException("Oauth 진행 중 데이터 파싱에 실패했습니다.");
		}
	}

	private void validateAccessTokenSuccess(final HttpResponse<String> response) {
		final JSONObject jsonObject = new JSONObject(response.body());
		if (jsonObject.get("code").equals(ERROR_CODE)) {
			throw new ConflictException("잘못된 하이웍스 로그인 요청입니다.");
		}
		final HttpStatus status = HttpStatus.resolve(response.statusCode());
		if (status.is5xxServerError()) {
			throw new InternalServerException("하이웍스 서버에 문제가 있습니다.");
		}
	}

	@Override
	public HiworksProfileResponse getProfile(final String accessToken) {
		try {
			final HttpResponse<String> profileResponse = requestProfile(accessToken);
			return parseProfile(profileResponse);
		} catch (IOException e) {
			throw new InternalServerException("Oauth 진행 중 예상치 못한 문제가 생겼습니다.");
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new InternalServerException("Oauth 진행 중 예상치 못한 문제가 생겼습니다.");
		}
	}

	private HttpResponse<String> requestProfile(final String accessToken) throws IOException, InterruptedException {
		final HttpRequest profileRequest = buildProfileRequest(accessToken);
		return httpClient.send(profileRequest, BodyHandlers.ofString());
	}

	private HttpRequest buildProfileRequest(final String accessToken) {
		return HttpRequest.newBuilder(toUri(profileUrl))
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
			.GET()
			.build();
	}

	private HiworksProfileResponse parseProfile(final HttpResponse<String> profileResponse) {
		try {
			return objectMapper.readValue(profileResponse.body(), HiworksProfileResponse.class);
		} catch (JsonProcessingException e) {
			throw new InternalServerException("Oauth 진행 중 데이터 파싱에 실패했습니다.");
		}
	}
}
