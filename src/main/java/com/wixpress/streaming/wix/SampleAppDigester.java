package com.wixpress.streaming.wix;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Created by : doron
 * Since: 8/27/12
 */
public class SampleAppDigester {

    ObjectMapper objectMapper = new ObjectMapper();

    public String serializeSampleAppInstance(Instance instance) {
        try {
            return objectMapper.writeValueAsString(instance);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Instance deserializeSampleAppInstance(String sampleAppInstanceJson) {
        try {
            return objectMapper.readValue(sampleAppInstanceJson, Instance.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
