package com.wixpress.streaming.wix;

import org.codehaus.jackson.map.ObjectMapper;

import javax.annotation.Resource;

/**
 * @author shaiyallin
 * @since 8/29/12
 */
public class BaseController {

    final String WIX_SECRET = "9828f4a5-d476-4a50-a482-410c026b1969";
    final static String WIX_APPLICATION_ID = "12aecc27-b747-63bb-e1f9-62b3d2ef542f";

    @Resource
    AppInstanceDao appInstanceDao;

    AuthenticationResolver authenticationResolver = new AuthenticationResolver(new ObjectMapper());
}
