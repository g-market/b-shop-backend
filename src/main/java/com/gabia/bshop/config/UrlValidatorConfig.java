package com.gabia.bshop.config;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class UrlValidatorConfig {

	private final String[] schema = {"http", "https"};
	@Bean
	public UrlValidator urlValidator() {
		return new UrlValidator(schema);
	}
}
