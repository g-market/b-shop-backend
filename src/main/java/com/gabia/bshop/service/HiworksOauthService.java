package com.gabia.bshop.service;

import com.gabia.bshop.dto.HiworksAccessToken;
import com.gabia.bshop.dto.HiworksUserResources;
import com.gabia.bshop.entity.Member;

public interface HiworksOauthService {

    HiworksAccessToken getAccessToken(String authorizationCode);
    HiworksUserResources getUserResources(HiworksAccessToken token);
    Member upsert(HiworksUserResources userResources);
}
