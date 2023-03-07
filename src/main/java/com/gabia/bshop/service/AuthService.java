package com.gabia.bshop.service;

import static com.gabia.bshop.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.response.AdminLoginResponse;
import com.gabia.bshop.dto.response.HiworksProfileResponse;
import com.gabia.bshop.dto.response.IssuedTokensResponse;
import com.gabia.bshop.dto.response.LoginResult;
import com.gabia.bshop.dto.response.MemberResponse;
import com.gabia.bshop.entity.Member;
import com.gabia.bshop.entity.enumtype.MemberRole;
import com.gabia.bshop.exception.ForbiddenException;
import com.gabia.bshop.exception.NotFoundException;
import com.gabia.bshop.exception.UnAuthorizedRefreshTokenException;
import com.gabia.bshop.mapper.HiworksProfileMapper;
import com.gabia.bshop.mapper.MemberResponseMapper;
import com.gabia.bshop.repository.MemberRepository;
import com.gabia.bshop.repository.RefreshTokenRepository;
import com.gabia.bshop.security.RefreshToken;
import com.gabia.bshop.security.client.HiworksOauthClient;
import com.gabia.bshop.security.provider.JwtProvider;
import com.gabia.bshop.security.provider.RefreshTokenProvider;
import com.gabia.bshop.config.DefaultImageProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

	private final HiworksOauthClient hiworksOauthClient;
	private final MemberRepository memberRepository;
	private final JwtProvider jwtProvider;
	private final RefreshTokenProvider refreshTokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;
	private final DefaultImageProperties defaultImageProperties;

	@Transactional
	public LoginResult login(final String authCode) {
		final HiworksProfileResponse hiworksProfileResponse = getHiworksProfileResponse(authCode);
		final Member member = addOrUpdateMember(hiworksProfileResponse);
		final Long memberId = member.getId();
		final String applicationAccessToken = jwtProvider.createAccessToken(memberId,
			member.getRole());
		final RefreshToken refreshToken = refreshTokenProvider.createToken(memberId);
		refreshTokenRepository.save(refreshToken);
		final MemberResponse memberResponse = MemberResponseMapper.INSTANCE.from(member);
		return new LoginResult(refreshToken.refreshToken(), applicationAccessToken, memberResponse);
	}

	public AdminLoginResponse loginAdmin(final String authCode) {
		final HiworksProfileResponse hiworksProfileResponse = getAdminHiworksProfileResponse(authCode);
		final String hiworksId = hiworksProfileResponse.hiworksId();
		final Member member = memberRepository.findByHiworksId(hiworksId)
			.orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND_EXCEPTION, hiworksId));
		if (!member.isAdmin()) {
			throw new ForbiddenException(NOT_ADMIN_EXCEPTION);
		}
		final String applicationAccessToken = jwtProvider.createAccessToken(member.getId(), member.getRole());
		return new AdminLoginResponse(applicationAccessToken);
	}

	@Transactional
	public IssuedTokensResponse issueAccessToken(final String refreshTokenValue) {
		final RefreshToken refreshToken = refreshTokenRepository.findToken(refreshTokenValue);
		checkExpired(refreshTokenValue, refreshToken);
		final Long memberId = refreshToken.memberId();
		final MemberRole memberRole = memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND_EXCEPTION, memberId))
			.getRole();
		final String newAccessToken = jwtProvider.createAccessToken(memberId, memberRole);
		final RefreshToken newRefreshToken = refreshTokenProvider.createToken(memberId);
		refreshTokenRepository.save(newRefreshToken);
		refreshTokenRepository.delete(refreshTokenValue);
		return IssuedTokensResponse.builder()
			.accessToken(newAccessToken)
			.refreshToken(newRefreshToken.refreshToken())
			.build();
	}

	private HiworksProfileResponse getHiworksProfileResponse(final String code) {
		final String hiworksAccessToken = hiworksOauthClient.getAccessToken(code);
		return hiworksOauthClient.getProfile(hiworksAccessToken);
	}

	private HiworksProfileResponse getAdminHiworksProfileResponse(final String code) {
		final String hiworksAccessToken = hiworksOauthClient.getAccessToken(code);
		return hiworksOauthClient.getProfile(hiworksAccessToken);
	}

	private Member addOrUpdateMember(final HiworksProfileResponse hiworksProfileResponse) {
		final Member requestedMember = HiworksProfileMapper.INSTANCE.hiworksProfileResponseToMember(hiworksProfileResponse);
		requestedMember.setDefaultProfileImageUrl(defaultImageProperties.getProfileImageUrl());
		final Member member = memberRepository.findByHiworksId(hiworksProfileResponse.hiworksId())
			.orElseGet(() -> memberRepository.save(requestedMember));
		member.updateEmailAndNameAndHiworksId(requestedMember);
		return member;
	}

	private void checkExpired(final String refreshToken, final RefreshToken tokenInfo) {
		if (tokenInfo.isExpired()) {
			refreshTokenRepository.delete(refreshToken);
			throw new UnAuthorizedRefreshTokenException(REFRESH_TOKEN_EXPIRED_EXCEPTION);
		}
	}
}
