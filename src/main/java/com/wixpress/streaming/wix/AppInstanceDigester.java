package com.wixpress.streaming.wix;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Created by : doron
 * Since: 8/27/12
 */
public class AppInstanceDigester {

    ObjectMapper objectMapper = new ObjectMapper();

    public String serializeSampleAppInstance(AppInstance appInstance) {
        try {
            return objectMapper.writeValueAsString(appInstance);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AppInstance deserializeSampleAppInstance(String sampleAppInstanceJson) {
        try {
            return objectMapper.readValue(sampleAppInstanceJson, AppInstance.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
