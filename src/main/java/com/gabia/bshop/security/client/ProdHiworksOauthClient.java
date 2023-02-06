package com.gabia.bshop.security.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabia.bshop.dto.request.HiworksTokenRequest;
import com.gabia.bshop.dto.response.HiworksProfileResponse;
import com.gabia.bshop.dto.response.HiworksTokenResponse;
import com.gabia.bshop.exception.ConflictException;
import com.gabia.bshop.exception.InternalServerException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProdHiworksOauthClient implements HiworksOauthClient {

	private static final String GRANT_TYPE = "authorization_code";
	private static final String ACCESS_TYPE = "offline";

	private final String clientId;
	private final String clientSecret;
	private final String accessTokenUrl;
	private final String profileUrl;

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final HttpClient httpClient = HttpClient.newBuilder()
		.version(HttpClient.Version.HTTP_2)
		.followRedirects(Redirect.NORMAL)
		.connectTimeout(Duration.ofSeconds(10))
		.build();

	@Override
	public String getAccessToken(final String authCode) {
		final HiworksTokenRequest hiworksTokenRequest = HiworksTokenRequest.builder()
			.clientId(clientId)
			.clientSecret(clientSecret)
			.grantType(GRANT_TYPE)
			.authCode(authCode)
			.accessType(ACCESS_TYPE)
			.build();
		final String requestBody = toRequestBody(hiworksTokenRequest);
		try {
			final HttpResponse<String> accessTokenResponse = requestAccessToken(requestBody);
			return parseAccessToken(accessTokenResponse);
		} catch (IOException e) {
			throw new InternalServerException("Oauth 진행 중 예상치 못한 문제가 생겼습니다.");
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new InternalServerException("Oauth 진행 중 예상치 못한 문제가 생겼습니다.");
		}
	}

	private String toRequestBody(final HiworksTokenRequest hiworksTokenRequest) {
		try {
			return objectMapper.writeValueAsString(hiworksTokenRequest);
		} catch (JsonProcessingException e) {
			throw new InternalServerException("Oauth 진행 중 예상치 못한 문제가 생겼습니다.");
		}
	}

	private HttpResponse<String> requestAccessToken(final String requestBody)
		throws IOException, InterruptedException {
		final HttpRequest accessTokenRequest = buildAccessTokenRequest(requestBody);
		return httpClient.send(accessTokenRequest, BodyHandlers.ofString());
	}

	private HttpRequest buildAccessTokenRequest(final String requestBody) {
		return HttpRequest.newBuilder(toUri(accessTokenUrl))
			.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
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
		validateStatusSuccess(response);
		try {
			final JSONObject jsonObject = new JSONObject(response);
			final HiworksTokenResponse hiworksTokenResponse =
				objectMapper.readValue(jsonObject.get("data").toString(), HiworksTokenResponse.class);
			return hiworksTokenResponse.accessToken();
		} catch (JsonProcessingException e) {
			throw new InternalServerException("Oauth 진행 중 데이터 파싱에 실패했습니다.");
		}
	}

	private void validateStatusSuccess(final HttpResponse<String> response) {
		final HttpStatus status = HttpStatus.resolve(response.statusCode());
		if (status == null || status.is4xxClientError()) {
			throw new ConflictException("잘못된 하이웍스 로그인 요청입니다.");
		}
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

	private HttpResponse<String> requestProfile(final String accessToken)
		throws IOException, InterruptedException {
		final HttpRequest profileRequest = buildProfileRequest(accessToken);
		return httpClient.send(profileRequest, BodyHandlers.ofString());
	}

	private HttpRequest buildProfileRequest(final String accessToken) {
		return HttpRequest
			.newBuilder(toUri(profileUrl))
			.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
			.GET()
			.build();
	}

	private HiworksProfileResponse parseProfile(final HttpResponse<String> profileResponse) {
		validateStatusSuccess(profileResponse);

		try {
			return objectMapper.readValue(profileResponse.body(), HiworksProfileResponse.class);
		} catch (JsonProcessingException e) {
			throw new InternalServerException("Oauth 진행 중 데이터 파싱에 실패했습니다.");
		}
	}
}
