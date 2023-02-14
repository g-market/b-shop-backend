package com.gabia.bshop.repository;

import com.gabia.bshop.security.RefreshToken;

public interface RefreshTokenRepository {

	RefreshToken save(RefreshToken refreshToken);

	RefreshToken findToken(String savedTokenValue);

	void delete(String savedTokenValue);
}
