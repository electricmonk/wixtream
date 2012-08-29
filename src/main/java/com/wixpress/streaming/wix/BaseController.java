package com.wixpress.streaming.wix;

import com.wixpress.streaming.wix.AuthenticationResolver;
import com.wixpress.streaming.wix.InstanceDao;
import org.codehaus.jackson.map.ObjectMapper;

import javax.annotation.Resource;

/**
 * @author shaiyallin
 * @since 8/29/12
 */
public class BaseController {

    final String WIX_SECRET = "39202616-8cfc-4a28-a8d7-4790d13de94e";
    final static String WIX_APPLICATION_ID = "129a90ff-094d-f193-49a0-2da5d7d2209b";

    @Resource
    InstanceDao instanceDao;

    AuthenticationResolver authenticationResolver = new AuthenticationResolver(new ObjectMapper());
}
