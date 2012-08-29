package com.wixpress.streaming.controller;

import com.wixpress.streaming.domain.AuthenticationResolver;
import com.wixpress.streaming.domain.InstanceDao;
import org.codehaus.jackson.map.ObjectMapper;

import javax.annotation.Resource;

/**
 * @author shaiyallin
 * @since 8/29/12
 */
public class BaseController {

    final String SECRET = "39202616-8cfc-4a28-a8d7-4790d13de94e";
    final static String APPLICATION_ID = "129a90ff-094d-f193-49a0-2da5d7d2209b";

    @Resource
    InstanceDao instanceDao;

    AuthenticationResolver authenticationResolver = new AuthenticationResolver(new ObjectMapper());
}
