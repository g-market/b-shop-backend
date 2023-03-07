package com.gabia.bshop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties("application.default")
public class DefaultImageProperties {

	private String profileImageUrl;
	private String itemImageUrl;
}
