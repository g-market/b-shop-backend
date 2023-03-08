package com.gabia.bshop.config;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import io.jsonwebtoken.security.Keys;

@SpringBootTest
class TokenPropertiesTest {

	@Autowired
	private TokenProperties tokenProperties;

	@Value("${token.secret}")
	private String secret;

	@Value("${token.access-expired-time}")
	private long accessExpiredTime;

	@Value("${token.refresh-expired-time}")
	private long refreshExpiredTime;

	@Test
	@DisplayName("주입이 정상적으로 되었는지 테스트 한다")
	void checkTokenProperties() {
		assertAll(
			() -> assertThat(tokenProperties.getSecret()).isEqualTo(secret),
			() -> assertThat(tokenProperties.getRefreshExpiredTime()).isEqualTo(refreshExpiredTime),
			() -> assertThat(tokenProperties.getAccessExpiredTime()).isEqualTo(accessExpiredTime),
			() -> assertThat(tokenProperties.getSecretKey()).isEqualTo(
				Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
		);
	}
}
