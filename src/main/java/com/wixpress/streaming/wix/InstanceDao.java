package com.wixpress.streaming.wix;

import com.google.appengine.api.datastore.*;

import java.util.UUID;

/**
 * Created by : doron
 * Since: 8/27/12
 */

public class InstanceDao  {

    static final String INSTANCE = "Instance";
    static final String BAGGAGE = "Baggage";

    private SampleAppDigester digester = new SampleAppDigester();
    private DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();

    public Instance addAppInstance(Instance instance) {
        Entity entity = new Entity(INSTANCE, instance.getInstanceId().toString());
        entity.setProperty(BAGGAGE, digester.serializeSampleAppInstance(instance));

        Transaction transaction = dataStore.beginTransaction();
        try {
            dataStore.put(entity);
            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
        return instance;
    }

    public Instance addAppInstance(WixSignedInstance wixSignedInstance) {
        return addAppInstance(new Instance(wixSignedInstance));
    }

    public Instance getAppInstance(UUID instanceId) {
        if (instanceId == null)
            return null;
        else {
            final Key key = KeyFactory.createKey(INSTANCE, instanceId.toString());
            try {
                final String baggage = dataStore.get(key).getProperty(BAGGAGE).toString();
                return digester.deserializeSampleAppInstance(baggage);
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public Instance getAppInstance(WixSignedInstance wixSignedInstance) {
        return getAppInstance(wixSignedInstance.getInstanceId());
    }

    public void update(Instance appInstance) {
        addAppInstance(appInstance);
    }
}
