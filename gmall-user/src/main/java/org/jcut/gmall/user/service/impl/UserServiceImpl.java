package org.jcut.gmall.user.service.impl;

import org.jcut.gmall.user.bean.UmsReceiveAddress;
import org.jcut.gmall.user.bean.UmsUser;
import org.jcut.gmall.user.mapper.ReceiveAddressMapper;
import org.jcut.gmall.user.mapper.UserMapper;
import org.jcut.gmall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    ReceiveAddressMapper receiveAddressMapper;

    @Override
    public List<UmsUser> getAllUser() {
        List<UmsUser> umsUserList=userMapper.selectAll();
        return umsUserList;
    }

    @Override
    public List<UmsReceiveAddress> getReceiveAdddresses(String uid) {
        UmsReceiveAddress t=new UmsReceiveAddress();
       t.setUid(uid);
        List<UmsReceiveAddress> umsReceiveAddresses=receiveAddressMapper.select(t);
        return umsReceiveAddresses;
    }


}
