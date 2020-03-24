package org.jcut.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.lang3.StringUtils;
import org.jcut.gmall.bean.PmsProductInfo;
import org.jcut.gmall.bean.SmsStore;
import org.jcut.gmall.service.StoreService;
import org.jcut.gmall.util.CookieUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

/*
店铺controller
 */
@Controller
@CrossOrigin
public class StoreController {
    @Reference
    StoreService storeService;

    @RequestMapping("getAllStore")
    @ResponseBody
    public List<SmsStore> getAllStore(HttpServletRequest request){
        //该操作需检验商城管理员是否登录
        String flag = CookieUtil.checkMallAdmin(request);
        if (flag.equals("success")){
            List<SmsStore> smsStoreList=storeService.getAllStore();
            return smsStoreList;
        }
        return null;

    }

    /**
     * 商家申请入驻
     * @param smsStore
     * @return
     */
    @RequestMapping("registerStore")
    @ResponseBody
    public String registerStore(SmsStore smsStore){
        if (smsStore==null){
            return "fail";
        }
        String sid = UUID.randomUUID().toString();
        smsStore.setSid(sid);
        smsStore.setStore_status("0");
        storeService.registerStore(smsStore);
        return "success";
    }


    /**
     * 商城管理员处理店铺入驻
     * @param request
     */
    @RequestMapping("handleSettle")
    @ResponseBody
    public String handleSettle(String sid,HttpServletRequest request){
        String flag = CookieUtil.checkMallAdmin(request);
        if (flag.equals("success")){
            storeService.handleSettle(sid);
            return "success";
        }
        return "fail";
    }

    /**
     * 得到申请入驻的商家列表
     * @param request
     * @return
     */
    @RequestMapping("getStoreByHandle")
    @ResponseBody
    public List<SmsStore> getStoreByHandle(HttpServletRequest request){
        String flag = CookieUtil.checkMallAdmin(request);
        if (flag.equals("success")){
            List<SmsStore> smsStoreList=storeService.getStoreByHandle();
            return smsStoreList;
        }
        return null;
    }

    @RequestMapping("getStoreBySid")
    @ResponseBody
    public SmsStore getStoreBySid(String sid,HttpServletRequest request){
        String flag = CookieUtil.checkMallAdmin(request);
        if (flag.equals("success")){
         SmsStore smsStore= storeService.getStoreBySid(sid);
         return smsStore;
        }
        return null;

    }





}
