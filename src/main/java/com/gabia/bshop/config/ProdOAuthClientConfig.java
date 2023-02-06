package com.gabia.bshop.config;

import com.gabia.bshop.security.client.HiworksOauthClient;
import com.gabia.bshop.security.client.LocalHiworksOauthClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "prod"})
public class ProdOAuthClientConfig {

    @Bean
    @Qualifier("normal")
    public HiworksOauthClient normalHiworksOauthClient(
            @Value("${hiworks.client.id}") final String clientId,
            @Value("${hiworks.client.secret}") final String secret,
            @Value("${hiworks.url.accessToken}") final String accessTokenUrl,
            @Value("${hiworks.url.user}") final String profileUrl) {
        return LocalHiworksOauthClient.builder()
                .clientId(clientId)
                .secret(secret)
                .accessTokenUrl(accessTokenUrl)
                .profileUrl(profileUrl)
                .build();
    }

    @Bean
    @Qualifier("admin")
    public HiworksOauthClient adminHiworksOauthClient(
            @Value("${hiworks.client.admin-id}") final String clientId,
            @Value("${hiworks.client.admin-secret}") final String secret,
            @Value("${hiworks.url.accessToken}") final String accessTokenUrl,
            @Value("${hiworks.url.user}") final String profileUrl) {
        return LocalHiworksOauthClient.builder()
                .clientId(clientId)
                .secret(secret)
                .accessTokenUrl(accessTokenUrl)
                .profileUrl(profileUrl)
                .build();
    }
}
