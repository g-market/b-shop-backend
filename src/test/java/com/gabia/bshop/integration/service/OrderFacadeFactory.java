package com.gabia.bshop.integration.service;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

import com.gabia.bshop.service.OrderFacadeService;
import com.gabia.bshop.service.OrderService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderFacadeFactory {

	private final OrderService orderService;

	private final RedisProperties redisProperties;

	public OrderFacadeService orderFacadeService() {
		return new OrderFacadeService(getRedissonClient(), orderService);
	}

	RedissonClient getRedissonClient() {
		Config redisConfig = new Config();

		redisConfig.useSingleServer()
			.setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort())
			.setConnectionMinimumIdleSize(5)
			.setConnectionPoolSize(5);

		return Redisson.create(redisConfig);
	}
}
