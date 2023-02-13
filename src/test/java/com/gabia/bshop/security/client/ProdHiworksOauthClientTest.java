package com.gabia.bshop.security.client;

import static org.assertj.core.api.Assertions.*;
import static org.mockserver.model.HttpRequest.*;
import static org.mockserver.model.HttpResponse.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabia.bshop.dto.response.HiworksProfileResponse;
import com.gabia.bshop.dto.response.HiworksTokenResponse;

import lombok.Builder;

@SpringBootTest
@DisplayName("hiworks Oauth 테스트")
class ProdHiworksOauthClientTest {

	@Autowired
	private ProdHiworksOauthClient prodHiworksOauthClient;

	@Autowired
	private ObjectMapper objectMapper;

	private ClientAndServer mockServer;

	@BeforeEach
	void setUp() throws JsonProcessingException {
		mockServer = ClientAndServer.startClientAndServer(8888);
		final MockServerClient mockServerClient = new MockServerClient("localhost", 8888);

		// hiworksAccessToken Valid
		mockServerClient
			.when(
				request()
					.withMethod("POST")
					.withPath("/login/oauth/access_token")
			)
			.respond(
				response()
					.withHeader(new Header("Content-Type", "application/json;charset=utf-8"))
					.withBody(objectMapper.writeValueAsString(hiworksAccessTokenResponse())
					)
			);

		// memberProfile
		mockServerClient
			.when(
				request()
					.withMethod("GET")
					.withPath("/user")
					.withHeader(new Header(HttpHeaders.AUTHORIZATION, "Bearer " + "accessToken"))
			)
			.respond(
				response()
					.withHeader(new Header("Content-Type", "application/json;charset=utf-8"))
					.withBody(objectMapper.writeValueAsString(new HiworksProfileResponse("1234567", "admin", "홍길동")))
			);
	}

	@AfterEach
	void shutDown() {
		mockServer.stop();
	}

	private HiworksAccessTokenResponse hiworksAccessTokenResponse() {
		final HiworksTokenResponse hiworksTokenResponse = HiworksTokenResponse.builder()
			.accessToken("il8nqtfus0y7ndlyym2zxyl4yw80jdt1")
			.refreshToken("rbwr5pb83zlvyve6s9zhbtttejr4ysj1")
			.officeNo(123123L)
			.userNo(1234567L)
			.build();
		return HiworksAccessTokenResponse.builder()
			.code("SUC")
			.msg("")
			.hiworksTokenResponse(hiworksTokenResponse)
			.build();
	}

	@DisplayName("인증 코드를 하이웍스 Oauth에 요청을 하면 HiworksTokenResponse에서 accessToken을 반환한다.")
	@Test
	void given_authCode_when_getAccessToken_then_return_hiworksAccessToken() {
		// given
		final String authCode = "authCode";

		// when & then
		final String accessToken = prodHiworksOauthClient.getAccessToken(authCode);
		assertThat(accessToken).isEqualTo("il8nqtfus0y7ndlyym2zxyl4yw80jdt1");
	}

	@DisplayName("accessToken을 하이웍스 Oauth에 요청을 하면 회원 정보를 반환한다.")
	@Test
	void given_accessToken_when_getProfile_then_return_hiworksAccessToken() {
		// given
		final String accessToken = "accessToken";
		final HiworksProfileResponse expect = new HiworksProfileResponse("1234567", "admin", "홍길동");

		// when & then
		final HiworksProfileResponse hiworksProfileResponse = prodHiworksOauthClient.getProfile(accessToken);
		assertThat(hiworksProfileResponse).usingRecursiveComparison().isEqualTo(expect);
	}

	@Builder
	record HiworksAccessTokenResponse(
		String code,
		String msg,
		@JsonProperty("data") HiworksTokenResponse hiworksTokenResponse) {
	}
}
