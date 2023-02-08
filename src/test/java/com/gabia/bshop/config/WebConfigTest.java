package com.gabia.bshop.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
	private static final String MAIN_SERVER_DOMAIN = "https://b-shop.app";

	@Autowired
	private MockMvc mockMvc;

	@ParameterizedTest
	@ValueSource(strings = {MAIN_SERVER_DOMAIN, "http://127.0.0.1", "http://b-shop.com"})
	void 특정_Origin에_CORS가_허용되어있다(final String origin) throws Exception {
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
	void CORS가_허용되지_않은_Origin에서_Preflight_요청을_보내면_허용하지_않는다() throws Exception {
		mockMvc.perform(
				options("/members/me")
					.header(HttpHeaders.ORIGIN, "http://not-allowed-origin.com")
					.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
			)
			.andExpect(status().isForbidden())
			.andDo(print());
	}
}
