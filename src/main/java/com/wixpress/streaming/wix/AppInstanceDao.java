package com.wixpress.streaming.wix;

import com.google.appengine.api.datastore.*;

import java.util.UUID;

/**
 * Created by : doron
 * Since: 8/27/12
 */

public class AppInstanceDao {

    static final String INSTANCE = "Instance";
    static final String BAGGAGE = "Baggage";

    private AppInstanceDigester digesterApp = new AppInstanceDigester();
    private DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();

    public AppInstance addAppInstance(AppInstance appInstance) {
        Entity entity = new Entity(INSTANCE, appInstance.getInstanceId().toString());
        entity.setProperty(BAGGAGE, digesterApp.serializeSampleAppInstance(appInstance));

        Transaction transaction = dataStore.beginTransaction();
        try {
            dataStore.put(entity);
            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
        return appInstance;
    }

    public AppInstance addAppInstance(WixSignedInstance wixSignedInstance) {
        return addAppInstance(new AppInstance(wixSignedInstance));
    }

    public AppInstance getAppInstance(UUID instanceId) {
        if (instanceId == null)
            return null;
        else {
            final Key key = KeyFactory.createKey(INSTANCE, instanceId.toString());
            try {
                final String baggage = dataStore.get(key).getProperty(BAGGAGE).toString();
                return digesterApp.deserializeSampleAppInstance(baggage);
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public AppInstance getAppInstance(WixSignedInstance wixSignedInstance) {
        return getAppInstance(wixSignedInstance.getInstanceId());
    }

    public void update(AppInstance appAppInstance) {
        addAppInstance(appAppInstance);
    }
}
