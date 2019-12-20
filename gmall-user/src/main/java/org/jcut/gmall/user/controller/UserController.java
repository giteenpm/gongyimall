package org.jcut.gmall.user.controller;

import org.jcut.gmall.user.bean.UmsReceiveAddress;
import org.jcut.gmall.user.bean.UmsUser;
import org.jcut.gmall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping("index")
    @ResponseBody
    public String index(){
        return "hello user";
    }

    //查询所有的用户信息
    @RequestMapping("getAllUser")
    @ResponseBody
    public List<UmsUser> getAllUser(){
        List<UmsUser> umsUser=userService.getAllUser() ;//userService.getAllUser();
        return umsUser;
    }

    @RequestMapping("getReceiveAddresses")
    @ResponseBody
    public List<UmsReceiveAddress> getReceiveAddresses(String uid){
        List<UmsReceiveAddress> umsReceiveAddresses=userService.getReceiveAdddresses(uid);
        return umsReceiveAddresses;
    }



}
