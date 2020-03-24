package org.jcut.gmall.service;

import org.jcut.gmall.bean.SmsStore;

import java.util.List;

public interface StoreService {
    public List<SmsStore> getAllStore() ;

    void registerStore(SmsStore smsStore);

    void handleSettle(String sid);

    List<SmsStore> getStoreByHandle();

    SmsStore getStoreBySid(String sid);

    void updateGidBySid(SmsStore smsStore);
}
