package com.gabia.bshop.controller;

import com.gabia.bshop.dto.HiworksAccessToken;
import com.gabia.bshop.dto.HiworksUserResources;
import com.gabia.bshop.dto.RedirectUrlResponse;
import com.gabia.bshop.service.HiworksOauthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OauthController {

    private final HiworksOauthService hiworksOauthService;

    private final String HIWORKS_OAUTH_LOGIN_PAGE_URL = "www.temp-url.com";
    private final String LOGIN_AFTER_REDIRECT_URL = "www.temp-redirect-url.com";

    @GetMapping("/auth/hiworks")
    public ResponseEntity<String> getOauthUrl() {
        return ResponseEntity.ok(HIWORKS_OAUTH_LOGIN_PAGE_URL);
    }

    @GetMapping("/login")
    public ResponseEntity<RedirectUrlResponse> login(@RequestParam("code") String authorizationCode) {
       // hiworksOauthService.login();
        HiworksAccessToken accessToken = hiworksOauthService.getAccessToken(authorizationCode);
        HiworksUserResources userResources = hiworksOauthService.getUserResources(accessToken);
        hiworksOauthService.upsert(userResources);
        return null;
        //return ResponseEntity.ok(new RedirectUrlResponse(LOGIN_AFTER_REDIRECT_URL));
    }
}
