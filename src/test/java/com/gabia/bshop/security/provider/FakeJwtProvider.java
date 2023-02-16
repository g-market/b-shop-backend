package com.gabia.bshop.security.provider;

import java.util.Date;

import com.gabia.bshop.entity.enumtype.MemberRole;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class FakeJwtProvider {

	private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";

	private final TokenProperties tokenProperties;

	public FakeJwtProvider(final TokenProperties tokenProperties) {
		this.tokenProperties = tokenProperties;
	}

	public String createAccessToken(final Long id) {
		final Date now = new Date();
		final Date validity = new Date(now.getTime() + tokenProperties.getAccessExpiredTime());

		return Jwts.builder()
			.setSubject(ACCESS_TOKEN_SUBJECT)
			.setIssuedAt(now)
			.setExpiration(validity)
			.claim("id", id)
			.signWith(tokenProperties.getSecretKey(), SignatureAlgorithm.HS256)
			.compact();
	}

	public String createAccessToken(final String id, final MemberRole role) {
		final Date now = new Date();
		final Date validity = new Date(now.getTime() + tokenProperties.getAccessExpiredTime());

		return Jwts.builder()
			.setSubject(ACCESS_TOKEN_SUBJECT)
			.setIssuedAt(now)
			.setExpiration(validity)
			.claim("id", id)
			.claim("role", role)
			.signWith(tokenProperties.getSecretKey(), SignatureAlgorithm.HS256)
			.compact();
	}

	public String createAccessToken(final Long id, final String role) {
		final Date now = new Date();
		final Date validity = new Date(now.getTime() + tokenProperties.getAccessExpiredTime());

		return Jwts.builder()
			.setSubject(ACCESS_TOKEN_SUBJECT)
			.setIssuedAt(now)
			.setExpiration(validity)
			.claim("id", id)
			.claim("role", role)
			.signWith(tokenProperties.getSecretKey(), SignatureAlgorithm.HS256)
			.compact();
	}
}
