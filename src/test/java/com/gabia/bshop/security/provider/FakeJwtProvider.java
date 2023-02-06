package com.gabia.bshop.security.provider;


import com.gabia.bshop.entity.enumtype.MemberRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;

public class FakeJwtProvider {

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";

    private final Key secretKey;
    private final long validityInMilliseconds;

    public FakeJwtProvider(
            @Value("${token.secret}") final String secretKey,
            @Value("${token.access-expired-time}") final long validityInMilliseconds) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createAccessToken(final Long id) {
        final Date now = new Date();
        final Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(ACCESS_TOKEN_SUBJECT)
                .setIssuedAt(now)
                .setExpiration(validity)
                .claim("id", id)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createAccessToken(final String id, final MemberRole role) {
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

    public String createAccessToken(final Long id, final String role) {
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
}