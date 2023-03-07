package com.gabia.bshop.security.provider;

import static com.gabia.bshop.exception.ErrorCode.*;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.gabia.bshop.config.TokenProperties;
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
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtProvider {

	private static final String TOKEN_TYPE = "Bearer";
	private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";

	private final AuthTokenExtractor authTokenExtractor;
	private final TokenProperties tokenProperties;

	public String createAccessToken(final Long id, final MemberRole role) {
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
			.setSigningKey(tokenProperties.getSecretKey())
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
		final Claims body = getClaimsJws(token).getBody();
		try {
			final Long id = body.get("id", Long.class);
			final MemberRole role = MemberRole.valueOf(body.get("role", String.class));
			return MemberPayload.builder()
				.id(id)
				.role(role)
				.build();
		} catch (RequiredTypeException | NullPointerException | IllegalArgumentException e) {
			throw new UnAuthorizedException(TOKEN_INVALID_FORMAT_EXCEPTION);
		}
	}
}
