package com.gabia.bshop.security.provider;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gabia.bshop.entity.enumtype.MemberRole;
import com.gabia.bshop.exception.UnAuthorizedException;
import com.gabia.bshop.security.MemberPayload;
import com.gabia.bshop.util.AuthTokenExtractor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.RequiredTypeException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Builder;

@Component
public class JwtProvider {

	private static final String TOKEN_TYPE = "Bearer";
	private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";

	private final AuthTokenExtractor authTokenExtractor;
	private final Key secretKey;
	private final long validityInMilliseconds;

	@Builder
	private JwtProvider(final AuthTokenExtractor authTokenExtractor,
		@Value("${token.access-expired-time}") final long validityInMilliseconds,
		@Value("${token.secret}") final String secretKey) {
		this.authTokenExtractor = authTokenExtractor;
		this.validityInMilliseconds = validityInMilliseconds;
		this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
	}

	public String createAccessToken(final Long id, final MemberRole role) {
		final Date now = new Date();
		final Date validity = new Date(now.getTime() + validityInMilliseconds);

		return Jwts.builder()
			.setSubject(ACCESS_TOKEN_SUBJECT)
			.setIssuedAt(now)
			.setExpiration(validity)
			.claim("id", id)
			.claim("role", role)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}

	public boolean isValidToken(final String authorizationHeader) {
		final String token = authTokenExtractor.extractToken(authorizationHeader, TOKEN_TYPE);
		try {
			final Jws<Claims> claims = getClaimsJws(token);
			return isAccessToken(claims) && isNotExpired(claims);
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	private Jws<Claims> getClaimsJws(final String token) {
		return Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token);
	}

	private boolean isAccessToken(final Jws<Claims> claims) {
		return claims.getBody()
			.getSubject()
			.equals(ACCESS_TOKEN_SUBJECT);
	}

	private boolean isNotExpired(final Jws<Claims> claims) {
		return claims.getBody()
			.getExpiration()
			.after(new Date());
	}

	public MemberPayload getPayload(final String authorizationHeader) {
		final String token = authTokenExtractor.extractToken(authorizationHeader, TOKEN_TYPE);
		Claims body = getClaimsJws(token).getBody();
		try {
			Long id = body.get("id", Long.class);
			MemberRole role = MemberRole.valueOf(body.get("role", String.class));
			return MemberPayload.builder()
				.id(id)
				.role(role)
				.build();
		} catch (RequiredTypeException | NullPointerException | IllegalArgumentException e) {
			throw new UnAuthorizedException("토큰이 잘못된 형식입니다.");
		}
	}
}
