package com.gabia.bshop.config;

import java.net.http.HttpClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabia.bshop.security.client.HiworksOauthClient;
import com.gabia.bshop.security.client.ProdHiworksOauthClient;

import lombok.RequiredArgsConstructor;

@Configuration
@Profile({"dev", "prod", "test"})
@RequiredArgsConstructor
public class ProdOAuthClientConfig {

	private final ObjectMapper objectMapper;

	@Bean
	public HiworksOauthClient productinoHiworksOauthClient(
		@Value("${hiworks.client.id}") final String clientId,
		@Value("${hiworks.client.secret}") final String secret,
		@Value("${hiworks.url.accessToken}") final String accessTokenUrl,
		@Value("${hiworks.url.user}") final String profileUrl) {
		return ProdHiworksOauthClient.builder()
			.clientId(clientId)
			.clientSecret(secret)
			.accessTokenUrl(accessTokenUrl)
			.profileUrl(profileUrl)
			.objectMapper(objectMapper)
			.httpClient(httpClient())
			.build();
	}

	@Bean
	public HttpClient httpClient() {
		return HttpClient.newBuilder()
			.version(HttpClient.Version.HTTP_2)
			.followRedirects(HttpClient.Redirect.NORMAL)
			.build();
	}
}
