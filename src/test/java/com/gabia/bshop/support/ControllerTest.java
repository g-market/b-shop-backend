package com.gabia.bshop.support;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabia.bshop.config.MinioConfig;
import com.gabia.bshop.config.UrlValidatorConfig;
import com.gabia.bshop.controller.AuthController;
import com.gabia.bshop.controller.CartController;
import com.gabia.bshop.controller.ImageController;
import com.gabia.bshop.controller.ItemController;
import com.gabia.bshop.controller.ItemImageController;
import com.gabia.bshop.controller.ItemOptionController;
import com.gabia.bshop.controller.MemberController;
import com.gabia.bshop.controller.OrderController;
import com.gabia.bshop.security.provider.JwtProvider;
import com.gabia.bshop.security.provider.RefreshTokenCookieProvider;
import com.gabia.bshop.security.provider.TokenProperties;
import com.gabia.bshop.service.AuthService;
import com.gabia.bshop.service.CartService;
import com.gabia.bshop.service.ImageService;
import com.gabia.bshop.service.ItemImageService;
import com.gabia.bshop.service.ItemOptionService;
import com.gabia.bshop.service.ItemService;
import com.gabia.bshop.service.MemberService;
import com.gabia.bshop.service.OrderService;
import com.gabia.bshop.util.AuthTokenExtractor;
import com.gabia.bshop.util.ImageValidator;

@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@Import({AuthTokenExtractor.class, JwtProvider.class, RestDocsConfig.class, RefreshTokenCookieProvider.class,
	UrlValidatorConfig.class, ImageValidator.class, MinioConfig.class})
@WebMvcTest({AuthController.class, CartController.class, ImageController.class, ItemController.class,
	ItemImageController.class, ItemOptionController.class,
	MemberController.class, OrderController.class})
public class ControllerTest {

	@MockBean
	protected AuthService authService;

	@MockBean
	protected CartService cartService;

	@MockBean
	protected ImageService imageService;

	@MockBean
	protected ItemService itemService;

	@MockBean
	protected ItemImageService itemImageService;

	@MockBean
	protected ItemOptionService itemOptionService;

	@MockBean
	protected MemberService memberService;

	@MockBean
	protected OrderService orderService;

	@MockBean
	protected JwtProvider jwtProvider;

	@MockBean
	protected TokenProperties tokenProperties;

	protected final ObjectMapper objectMapper;

	public ControllerTest() {
		this.objectMapper = new ObjectMapper();
	}
}

