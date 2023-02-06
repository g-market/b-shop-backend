package com.gabia.bshop.security.redis;

import com.gabia.bshop.exception.UnAuthorizedException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public RefreshToken save(final RefreshToken refreshToken) {
        refreshTokenRepository.findById(refreshToken.refreshToken())
                .ifPresent(it -> {
                    throw new UnAuthorizedException("예상보다 더 많은 리프레시 토큰이 서버에 반영됐습니다.");
                });
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional(readOnly = true)
    public RefreshToken findToken(final String savedTokenValue) {
        return refreshTokenRepository.findById(savedTokenValue)
                .orElseThrow(() -> new UnAuthorizedException("서버에 존재하지 않는 리프레시 토큰입니다."));
    }

    @Override
    @Transactional
    public void delete(final String savedTokenValue) {
        final RefreshToken refreshToken = refreshTokenRepository.findById(savedTokenValue)
                .orElseThrow(() -> new EntityNotFoundException(
                                "사용자 고유 refreshToken : " + savedTokenValue
                                        + "로 등록된 리프테쉬 토큰이 없습니다."));
        refreshTokenRepository.delete(refreshToken);
    }
}
