package com.gabia.bshop.security.client;

import com.gabia.bshop.dto.response.HiworksProfileResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LocalHiworksOauthClient implements HiworksOauthClient {

    private final String clientId;
    private final String secret;
    private final String accessTokenUrl;
    private final String profileUrl;

    @Builder
    private LocalHiworksOauthClient(final String clientId, final String secret,
            final String accessTokenUrl,
            final String profileUrl) {
        this.clientId = clientId;
        this.secret = secret;
        this.accessTokenUrl = accessTokenUrl;
        this.profileUrl = profileUrl;
    }

    @Override
    public String getAccessToken(final String authCode) {
        if (authCode.startsWith("normal")) {
            return "normal" + authCode.substring(6, 8);
        }
        return "admin" + authCode.substring(5, 7);
    }

    @Override
    public HiworksProfileResponse getProfile(final String accessToken) {
        if (accessToken.startsWith("normal")) {
            return HiworksProfileResponse.builder()
                    .hiworksId(accessToken)
                    .email(accessToken + "@gabia.com")
                    .name(accessToken)
                    .build();
        }
        return HiworksProfileResponse.builder()
                .hiworksId(accessToken)
                .email(accessToken + "@gabia.com")
                .name(accessToken)
                .build();
    }
}
