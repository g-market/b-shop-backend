package com.gabia.bshop.controller;

import static com.gabia.bshop.exception.ErrorCode.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.gabia.bshop.dto.response.LoginResult;
import com.gabia.bshop.entity.Member;
import com.gabia.bshop.fixture.MemberFixture;
import com.gabia.bshop.mapper.MemberResponseMapper;
import com.gabia.bshop.service.AuthService;
import com.gabia.bshop.support.ControllerTest;
import com.gabia.bshop.support.ErrorCodeSnippet;

class AuthControllerTest extends ControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private AuthService authService;

	@Test
	@DisplayName("로그인 성공")
	void loginSuccess() throws Exception {
		// given
		final String authCode = "authCode";
		final String accessToken = "token";
		final String refreshToken = "refreshTokenValue";
		final Member jaime = MemberFixture.JAIME.getInstance(1L);
		final LoginResult loginResult = new LoginResult(refreshToken, accessToken, MemberResponseMapper.INSTANCE.from(jaime));

		given(authService.login(authCode)).willReturn(loginResult);

		// when
		ResultActions resultActions = mockMvc.perform(
			get("/login?auth_code=" + authCode)
		);

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(header().string("Set-cookie", containsString("refreshToken=" + refreshToken)))
			.andDo(print())
			.andDo(
				document("auth-login",
					new ErrorCodeSnippet(HIWORKS_AUTH_CODE_INVALID_EXCEPTION, OAUTH_PROCESSING_EXCEPTION, OAUTH_JSON_PARSING_EXCEPTION,
						HIWORKS_SERVER_ERROR_EXCEPTION))
			);

		verify(authService).login(authCode);
	}
}
