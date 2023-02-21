package com.gabia.bshop.security.provider;

import java.nio.charset.StandardCharsets;
import java.security.Key;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("token")
public class TokenProperties {

	private String secret;

	private long accessExpiredTime;

	private long refreshExpiredTime;

	private Key secretKey;

	@PostConstruct
	private void initKey() {
		secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}
}
