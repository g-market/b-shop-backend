package com.gabia.bshop.repository;

import static com.gabia.bshop.exception.ErrorCode.*;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.config.TokenProperties;
import com.gabia.bshop.exception.UnAuthorizedRefreshTokenException;
import com.gabia.bshop.security.RefreshToken;
import com.gabia.bshop.util.RedisValueSupport;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

	private static final String REFRESH_TOKEN_PREFIX = "refreshToken-";

	private final RedisTemplate<String, String> redisTemplate;
	private final StringRedisTemplate stringRedisTemplate;
	private final RedisValueSupport redisValueSupport;
	private final TokenProperties tokenProperties;

	@Override
	@Transactional
	public RefreshToken save(final RefreshToken refreshToken) {
		final String key = getKey(refreshToken.refreshToken());
		final String foundValue = getValue(key);
		if (foundValue != null) {
			throw new UnAuthorizedRefreshTokenException(REFRESH_TOKEN_DUPLICATED_SAVED_EXCEPTION);
		}
		final String value = redisValueSupport.writeValueAsString(refreshToken);
		stringRedisTemplate.opsForValue().set(key, value);
		stringRedisTemplate.expire(key, tokenProperties.getRefreshExpiredTime(), TimeUnit.MILLISECONDS);
		return refreshToken;
	}

	@Override
	public RefreshToken findToken(final String savedTokenValue) {
		final String key = getKey(savedTokenValue);
		final String foundValue = getValue(key);
		if (foundValue == null) {
			throw new UnAuthorizedRefreshTokenException(REFRESH_TOKEN_NOT_FOUND_EXCEPTION);
		}
		return redisValueSupport.readValue(foundValue, RefreshToken.class);
	}

	@Override
	@Transactional
	public void delete(final String savedTokenValue) {
		final String key = getKey(savedTokenValue);
		redisTemplate.delete(key);
	}

	private String getKey(final String refreshToken) {
		return REFRESH_TOKEN_PREFIX + refreshToken;
	}

	private String getValue(final String key) {
		return redisTemplate.opsForValue().get(key);
	}
}
