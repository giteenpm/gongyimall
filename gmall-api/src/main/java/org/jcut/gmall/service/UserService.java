package org.jcut.gmall.service;
import org.jcut.gmall.bean.SmsStore;
import org.jcut.gmall.bean.UmsAdmin;
import org.jcut.gmall.bean.UmsReceiveAddress;
import org.jcut.gmall.bean.UmsUser;

import java.util.List;

public interface UserService {
   List<UmsUser> getAllUser();


   List<UmsReceiveAddress> getReceiveAddresses(String uid);

    UmsUser getSingleUser(String uid);

    void deleteUserByUid(String uid);

    void updataUser(UmsUser umsUser);

    List<UmsUser> selectUserByXml();

    UmsUser login(UmsUser umsUser);

    void addUserToken(String token, String uid);

    void setDefaultAddress(String id);

    UmsReceiveAddress getSingleAddress(String id);

    SmsStore loginByAdmin(SmsStore smsStore);

 UmsAdmin loginByMallAdmin(UmsAdmin umsAdmin);

    void register(UmsUser umsUser);
}
