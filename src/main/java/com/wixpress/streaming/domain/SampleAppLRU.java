package com.wixpress.streaming.domain;

import java.util.UUID;

/**
 * Created by : doron
 * Since: 8/27/12
 */

public class SampleAppLRU extends SampleApp {

    protected final static Integer MAX_NUMBER_OF_INSTANCES = 20;

    // Instead of DB..
    private LRUCache<UUID, Instance> sampleAppInstanceMap = new LRUCache<UUID, Instance>(MAX_NUMBER_OF_INSTANCES);

    @Override
    public Instance addAppInstance(Instance instance) {
        return sampleAppInstanceMap.put(instance.getInstanceId(), instance);
    }

    @Override
    public Instance addAppInstance(WixSignedInstance wixSignedInstance) {
        Instance instance = new Instance(wixSignedInstance);
        sampleAppInstanceMap.put(instance.getInstanceId(), instance);
        return instance;
    }

    @Override
    public Instance getAppInstance(WixSignedInstance wixSignedInstance) {
        return getAppInstance(wixSignedInstance.getInstanceId());
    }

    @Override
    public Instance getAppInstance(UUID instanceId) {
        if (instanceId == null)
            return null;
        else
            return sampleAppInstanceMap.get(instanceId);
    }

    @Override
    public void update(Instance appInstance) {
        //TODO
    }

    public LRUCache<UUID, Instance> getSampleAppInstanceMap() {
        return sampleAppInstanceMap;
    }
}
