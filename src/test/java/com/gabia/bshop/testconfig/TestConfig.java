package com.gabia.bshop.testconfig;

import org.redisson.api.RedissonClient;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class TestConfig {

	@MockBean
	private RedissonClient redisClient;
}
