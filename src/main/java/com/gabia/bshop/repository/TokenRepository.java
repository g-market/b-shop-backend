package com.gabia.bshop.repository;

import java.util.Optional;

import com.gabia.bshop.security.redis.RefreshToken;

public interface TokenRepository {

	RefreshToken save(RefreshToken refreshToken);

	Optional<RefreshToken> findToken(String savedTokenValue);

	void delete(String savedTokenValue);
}
