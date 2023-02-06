package com.gabia.bshop.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.gabia.bshop.security.client.HiworksOauthClient;
import com.gabia.bshop.security.client.LocalHiworksOauthClient;

@Configuration
@Profile({"dev", "prod"})
public class ProdOAuthClientConfig {

	@Bean
	public HiworksOauthClient productinoHiworksOauthClient(
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
}
