package com.gabia.bshop.security.redis;

import static com.gabia.bshop.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.exception.UnAuthorizedException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	@Transactional
	public RefreshToken save(final RefreshToken refreshToken) {
		refreshTokenRepository.findById(refreshToken.refreshToken())
			.ifPresent(it -> {
				throw new UnAuthorizedException(REFRESH_TOKEN_DUPLICATED_SAVED_EXCEPTION);
			});
		return refreshTokenRepository.save(refreshToken);
	}

	@Override
	@Transactional(readOnly = true)
	public RefreshToken findToken(final String savedTokenValue) {
		return refreshTokenRepository.findById(savedTokenValue)
			.orElseThrow(() -> new UnAuthorizedException(REFRESH_TOKEN_NOT_FOUND_EXCEPTION));
	}

	@Override
	@Transactional
	public void delete(final String savedTokenValue) {
		final RefreshToken refreshToken = refreshTokenRepository.findById(savedTokenValue)
			.orElseThrow(() -> new UnAuthorizedException(REFRESH_TOKEN_NOT_FOUND_EXCEPTION));
		refreshTokenRepository.delete(refreshToken);
	}
}
