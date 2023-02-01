package com.gabia.bshop.service;

import com.gabia.bshop.dto.HiworksAccessToken;
import com.gabia.bshop.dto.HiworksUserResources;
import com.gabia.bshop.entity.Member;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("local")
@Service
public class LocalHiworksOauthServiceImpl implements HiworksOauthService {

    @Override
    public HiworksAccessToken getAccessToken(String authorizationCode) {
        return null;
    }

    @Override
    public HiworksUserResources getUserResources(HiworksAccessToken token) {
        return null;
    }

    @Override
    public Member upsert(HiworksUserResources userResources) {
        return null;
    }
}
