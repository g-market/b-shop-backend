package com.gabia.bshop.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.gabia.bshop.security.support.AuthArgumentResolver;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

	private static final String CORS_ALLOWED_METHODS = "GET,POST,HEAD,PUT,PATCH,DELETE,TRACE,OPTIONS";
	private static final String FRONTEND_LOCALHOST = "http://localhost:80";
	private static final String FROTNED_LOCAL_DOMAIN = "http://b-shop.com";
	private static final String MAIN_SERVER_DOMAIN = "https://b-shop.app";

	private final List<HandlerInterceptor> interceptors;
	private final AuthArgumentResolver authArgumentResolver;

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		interceptors.forEach(registry::addInterceptor);
	}

	@Override
	public void addCorsMappings(final CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedMethods(CORS_ALLOWED_METHODS.split(","))
			.allowedOrigins(MAIN_SERVER_DOMAIN, FRONTEND_LOCALHOST, FROTNED_LOCAL_DOMAIN)
			.allowCredentials(true)
			.exposedHeaders(HttpHeaders.LOCATION, HttpHeaders.SET_COOKIE);
	}

	@Override
	public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(authArgumentResolver);
	}
}
