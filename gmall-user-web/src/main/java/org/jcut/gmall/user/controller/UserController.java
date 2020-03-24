package org.jcut.gmall.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.jcut.gmall.annotations.LoginRequired;
import org.jcut.gmall.bean.SmsStore;
import org.jcut.gmall.bean.UmsAdmin;
import org.jcut.gmall.bean.UmsReceiveAddress;
import org.jcut.gmall.bean.UmsUser;
import org.jcut.gmall.service.UserService;

import org.jcut.gmall.util.AESUtil;
import org.jcut.gmall.util.CookieUtil;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Controller
@CrossOrigin
public class UserController {
    @Reference
    UserService userService;


    @RequestMapping("register")
    @ResponseBody
    public String register(UmsUser umsUser){
        if (umsUser==null){
            return "false";
        }
        String uid = UUID.randomUUID().toString();
        umsUser.setUid(uid);
        userService.register(umsUser);
        return "success";
    }

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

    //根据用户id查询用户信息
    @RequestMapping("getSingleUser")
    @ResponseBody
    public UmsUser getSingleUser(String uid){
        UmsUser users=userService.getSingleUser(uid);
        return users;
    }

    /*
    * 删除用户信息
    * */
    @RequestMapping("deleteUserByUid")
    @ResponseBody
    public String deleteUserByUid(String uid){
        if(uid==null){
            return "请键入正确的uid";
        }else{
            userService.deleteUserByUid(uid);
            return "删除成功";
        }

    }

    /*
    * 更新用户信息
    * */
    @RequestMapping("updateUser")
    @ResponseBody
    public String updateUser(UmsUser umsUser){

        userService.updataUser(umsUser);
        return "更新用户信息成功";
    }


    @RequestMapping("getReceiveAddresses")
    @ResponseBody
    @LoginRequired(loginSuccess = true)
    public List<UmsReceiveAddress> getReceiveAddresses(String uid, HttpServletRequest request, HttpServletResponse response){
        uid = (String) request.getAttribute("uid");
        List<UmsReceiveAddress> umsReceiveAddresses=userService.getReceiveAddresses(uid);
        return umsReceiveAddresses;
    }

    @RequestMapping("setDefaultAddress")
    @ResponseBody
    public void setDefaultAddress(String id){
        userService.setDefaultAddress(id);
    }


    /**
     * 店铺管理员登录
     * @return
     */
    @RequestMapping("loginByAdmin")
    @ResponseBody
    public String loginByAdmin(SmsStore smsStore,HttpServletRequest request,HttpServletResponse response){
       SmsStore storeInfo= userService.loginByAdmin(smsStore);
       if (storeInfo!=null){
           String sid=storeInfo.getSid();
           String admin_name = storeInfo.getAdmin_name();
           String admin_pwd = storeInfo.getAdmin_pwd();
           String content=sid+","+admin_name+","+admin_pwd;
           try {
               String info = AESUtil.encrypt(content, "key_value_length");
               CookieUtil.setCookie(request,response,"storeInfo",info,60*60*5,true);
                return "success";
           } catch (Exception e) {
               e.printStackTrace();
           }

       }
       return "fail";

    }

    /**
     * 验证管理员是否登录
     */
/*    @RequestMapping("checkLoginAdmin")
    @ResponseBody
    public boolean checkLoginAdmin(HttpServletRequest request,HttpServletResponse response){
        response.setHeader("Access-control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", request.getMethod());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
        //防止乱码，适用于传输JSON数据
        response.setHeader("Content-Type", "application/json;charset=UTF-8");

        String content=CookieUtil.getCookieValue(request, "storeInfo", false);
       if (StringUtils.isNotBlank(content)){
           try {
               String adminInfo = AESUtil.decrypt(content, "key_value_length");
               if (StringUtils.isNotBlank(adminInfo)){
                   String[] strings = adminInfo.split(",");
                  request.setAttribute("sid",strings[0]);
                   return true;
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
       return false;

    }*/

    /**
     * 商城管理员登录
     * @param umsAdmin
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("loginByMallAdmin")
    @ResponseBody
    public String loginByMallAdmin(UmsAdmin umsAdmin,HttpServletRequest request, HttpServletResponse response){
       UmsAdmin umsAdminInfo= userService.loginByMallAdmin(umsAdmin);
        if (umsAdminInfo!=null){
            String aname = umsAdminInfo.getAname();
            String pwd = umsAdminInfo.getPwd();
            String content=aname+","+pwd;
            try {
                String info = AESUtil.encrypt(content, "key_value_length");
                //将账号密码加密生成的字符串存储到cookie里
                CookieUtil.setCookie(request,response,"mallAdminInfo",info,60*60*5,true);
                return "success";
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return "fail";
    }
    





}
