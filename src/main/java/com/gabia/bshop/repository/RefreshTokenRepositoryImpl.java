package com.gabia.bshop.repository;

import static com.gabia.bshop.exception.ErrorCode.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.exception.UnAuthorizedRefreshTokenException;
import com.gabia.bshop.security.RefreshToken;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

	private final RedisTemplate<String, Object> redisTemplate;

	private final HashOperations<String, byte[], byte[]> hashOperations;

	private final HashMapper<Object, byte[], byte[]> hashMapper;

	@Value("${token.refresh-expired-time}")
	long expireLength;

	@Override
	@Transactional
	public RefreshToken save(final RefreshToken refreshToken) {
		final String key = refreshToken.refreshToken();
		if (hasSameRefreshToken(key)) {
			throw new UnAuthorizedRefreshTokenException(REFRESH_TOKEN_DUPLICATED_SAVED_EXCEPTION);
		}

		final Map<byte[], byte[]> mappedHash = hashMapper.toHash(refreshToken);
		hashOperations.putAll(key, mappedHash);
		redisTemplate.expire(key, expireLength, TimeUnit.MILLISECONDS);
		return refreshToken;
	}

	@Override
	public RefreshToken findToken(final String savedTokenValue) {
		final Map<byte[], byte[]> entries = hashOperations.entries(savedTokenValue);
		hasRefreshTokenEntries(entries);
		return convertRefreshToken(entries);
	}

	@Override
	@Transactional
	public void delete(final String savedTokenValue) {
		final Map<byte[], byte[]> entries = hashOperations.entries(savedTokenValue);
		hasRefreshTokenEntries(entries);
		redisTemplate.delete(savedTokenValue);
	}

	private boolean hasSameRefreshToken(final String key) {
		return !hashOperations.entries(key).isEmpty();
	}

	private void hasRefreshTokenEntries(final Map<byte[], byte[]> entries) {
		if (entries.isEmpty()) {
			throw new UnAuthorizedRefreshTokenException(REFRESH_TOKEN_NOT_FOUND_EXCEPTION);
		}
	}

	private RefreshToken convertRefreshToken(final Map<byte[], byte[]> entries) {
		return (RefreshToken)hashMapper.fromHash(entries);
	}
}
