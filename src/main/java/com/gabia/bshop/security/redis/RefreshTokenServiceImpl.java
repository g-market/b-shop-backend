package com.gabia.bshop.security.redis;

import static com.gabia.bshop.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.exception.UnAuthorizedRefreshTokenException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	@Transactional
	public RefreshToken save(final RefreshToken refreshToken) {
		hasSameRefreshToken(refreshToken.refreshToken());
		return refreshTokenRepository.save(refreshToken);
	}

	@Override
	@Transactional(readOnly = true)
	public RefreshToken findToken(final String savedTokenValue) {
		return findRefreshTokenById(savedTokenValue);
	}

	@Override
	@Transactional
	public void delete(final String savedTokenValue) {
		final RefreshToken refreshToken = findRefreshTokenById(savedTokenValue);
		refreshTokenRepository.delete(refreshToken);
	}

	private void hasSameRefreshToken(final String key) {
		refreshTokenRepository.findById(key)
			.ifPresent(it -> {
				throw new UnAuthorizedRefreshTokenException(REFRESH_TOKEN_DUPLICATED_SAVED_EXCEPTION);
			});
	}

	private RefreshToken findRefreshTokenById(final String savedTokenValue) {
		return refreshTokenRepository.findById(savedTokenValue)
			.orElseThrow(() -> new UnAuthorizedRefreshTokenException(REFRESH_TOKEN_NOT_FOUND_EXCEPTION));
	}
}
