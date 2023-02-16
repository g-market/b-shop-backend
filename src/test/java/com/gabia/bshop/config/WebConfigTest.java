package com.gabia.bshop.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class WebConfigTest {

	private static final String CORS_ALLOWED_METHODS = "GET,POST,HEAD,PUT,PATCH,DELETE,TRACE,OPTIONS";
	private static final String CORS_ALLOWED_HEADERS = String.join(", ", HttpHeaders.LOCATION,
		HttpHeaders.SET_COOKIE);

	@Autowired
	private MockMvc mockMvc;

	@ParameterizedTest
	@ValueSource(strings = {"http://b-shop.com", "https://b-shop.com", "https://123.456.789.1", "http://127.0.0.1"})
	@DisplayName("특정 Origin에 CORS가 허용되어있다")
	void given_origin_when_request_allowedOrigin_then_allow(final String origin) throws Exception {
		mockMvc.perform(
				options("/members/me")
					.header(HttpHeaders.ORIGIN, origin)
					.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
			)
			.andExpect(status().isOk())
			.andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin))
			.andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
				CORS_ALLOWED_METHODS))
			.andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
				CORS_ALLOWED_HEADERS))
			.andDo(print());
	}

	@Test
	@DisplayName("CORS가 허용되지 않은 Origin에서 Preflight 요청을 보내면 허용하지 않는다")
	void given_origin_when_request_unAllowedOrigin_then_return_httpStatusForbidden() throws Exception {
		mockMvc.perform(
				options("/members/me")
					.header(HttpHeaders.ORIGIN, "http://not-allowed-origin.com")
					.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
			)
			.andExpect(status().isForbidden())
			.andDo(print());
	}
}
