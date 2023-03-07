package com.gabia.bshop.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.gabia.bshop.security.support.AuthArgumentResolver;
import com.gabia.bshop.util.validator.PaginationArgumentResolver;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

	private static final String CORS_ALLOWED_METHODS = "GET,POST,HEAD,PUT,PATCH,DELETE,TRACE,OPTIONS";
	private static final String FRONTEND_LOCALHOST = "http://127.0.0.1";

	private final List<HandlerInterceptor> interceptors;
	private final AuthArgumentResolver authArgumentResolver;
	private final PaginationArgumentResolver paginationArgumentResolver;

	@Value("${server.local.domain}")
	private String localDomain;

	@Value("${server.prod.domain}")
	private String productionDomain;

	@Value("${server.prod.url}")
	private String productionUrl;

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		interceptors.forEach(registry::addInterceptor);
	}

	@Override
	public void addCorsMappings(final CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedMethods(CORS_ALLOWED_METHODS.split(","))
			.allowedOrigins(localDomain, FRONTEND_LOCALHOST, productionDomain, productionUrl)
			.allowCredentials(true)
			.exposedHeaders(HttpHeaders.LOCATION, HttpHeaders.SET_COOKIE);
	}

	@Override
	public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(authArgumentResolver);
		resolvers.add(paginationArgumentResolver);
	}
}
