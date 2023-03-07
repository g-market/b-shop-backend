package com.gabia.bshop.config;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UrlValidatorConfig {

	private final String[] schema = {"http"};

	@Bean
	public UrlValidator urlValidator() {
		return new UrlValidator(schema);
	}
}
