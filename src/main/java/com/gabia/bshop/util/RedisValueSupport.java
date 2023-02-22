package com.gabia.bshop.util;

import static com.gabia.bshop.exception.ErrorCode.*;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabia.bshop.exception.InternalServerException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RedisValueSupport {

	private final ObjectMapper objectMapper;

	public <T> String writeValueAsString(final T obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new InternalServerException(REDIS_JSON_PARSING_EXCEPTION);
		}
	}

	public <T> T readValue(final String jsonString, Class<T> valueType) {
		try {
			return objectMapper.readValue(jsonString, valueType);
		} catch (JsonProcessingException e) {
			throw new InternalServerException(REDIS_JSON_PARSING_EXCEPTION);
		}
	}
}
