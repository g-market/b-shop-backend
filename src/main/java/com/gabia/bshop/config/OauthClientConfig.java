package com.gabia.bshop.config;

import java.net.http.HttpClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabia.bshop.security.client.HiworksOauthClient;
import com.gabia.bshop.security.client.LocalHiworksOauthClient;
import com.gabia.bshop.security.client.ProdHiworksOauthClient;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class OauthClientConfig {

	private final ObjectMapper objectMapper;

	@Bean
	@Profile({"dev", "prod", "test"})
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
	@Profile("local")
	public HiworksOauthClient localHiworksOauthClient(
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
	@Profile({"dev", "prod", "test"})
	public HttpClient httpClient() {
		return HttpClient.newBuilder()
			.version(HttpClient.Version.HTTP_2)
			.followRedirects(HttpClient.Redirect.NORMAL)
			.build();
	}
}
