package org.jcut.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.jcut.gmall.bean.OmsOrder;
import org.jcut.gmall.bean.SmsStore;
import org.jcut.gmall.manage.mapper.StoreMapper;
import org.jcut.gmall.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
@Service
public class StoreServiceImpl implements StoreService {
    @Autowired
    StoreMapper storeMapper;

    @Override
    public List<SmsStore> getAllStore() {
        SmsStore smsStore=new SmsStore();
        smsStore.setStore_status("1");
        storeMapper.select(smsStore);
        List<SmsStore> smsStoreList = storeMapper.select(smsStore);
        return smsStoreList;
    }

    @Override
    public void registerStore(SmsStore smsStore) {
        storeMapper.insertSelective(smsStore);
    }

    @Override
    public void handleSettle(String sid) {
        SmsStore smsStore=new SmsStore();
        smsStore.setStore_status("1");
        Example example=new Example(SmsStore.class);
        example.createCriteria().andEqualTo("sid",sid);
        storeMapper.updateByExampleSelective(smsStore,example);
    }

    @Override
    public List<SmsStore> getStoreByHandle() {
        SmsStore smsStore=new SmsStore();
        smsStore.setStore_status("0");
        storeMapper.select(smsStore);
        List<SmsStore> smsStoreList = storeMapper.select(smsStore);
        return smsStoreList;
    }

    @Override
    public SmsStore getStoreBySid(String sid) {
        SmsStore smsStore=new SmsStore();
        smsStore.setSid(sid);
        SmsStore smsStore1 = storeMapper.selectOne(smsStore);
        return smsStore1;

    }

    //管理员修改本店铺参加的公益项目
    @Override
    public void updateGidBySid(SmsStore smsStore) {
        Example example=new Example(SmsStore.class);
        example.createCriteria().andEqualTo("sid",smsStore.getSid());
        storeMapper.updateByExampleSelective(smsStore,example);
    }
}
