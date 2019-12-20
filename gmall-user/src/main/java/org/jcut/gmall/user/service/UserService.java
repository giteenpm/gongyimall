package org.jcut.gmall.user.service;

import org.jcut.gmall.user.bean.UmsReceiveAddress;
import org.jcut.gmall.user.bean.UmsUser;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
   List<UmsUser> getAllUser();


   List<UmsReceiveAddress> getReceiveAdddresses(String uid);
}
