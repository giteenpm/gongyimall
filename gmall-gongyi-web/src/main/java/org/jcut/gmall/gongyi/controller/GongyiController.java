package org.jcut.gmall.gongyi.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.jcut.gmall.bean.GmsGongyi;
import org.jcut.gmall.bean.SmsStore;
import org.jcut.gmall.service.GongyiService;
import org.jcut.gmall.service.StoreService;
import org.jcut.gmall.util.CookieUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@CrossOrigin
public class GongyiController {
    @Reference
    GongyiService gongyiService;
    @Reference
    StoreService storeService;
    /**
     * 超级管理员发布公益项目
     * @param gmsGongyi
     * @param request
     * @return
     */
   @RequestMapping("publishGongyi")
    @ResponseBody
    public String publishGongyi(GmsGongyi gmsGongyi, HttpServletRequest request){
        String flag = CookieUtil.checkMallAdmin(request);
        if (flag.equals("success")){
            String gid = UUID.randomUUID().toString();
            gmsGongyi.setGid(gid);
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
            String create_time = dateFormat.format(date);
            gmsGongyi.setCreate_time(create_time);
         gongyiService.publishGongyi(gmsGongyi);
            return "success";
        }
        return "fail";
    }

    /**
     * 获取公益列表
     */
    @RequestMapping("getAllGongyi")
    @ResponseBody
    public List<GmsGongyi> getAllGongyi(){
      List<GmsGongyi> gmsGongyiList=  gongyiService.getAllGongyi();
      return gmsGongyiList;
    }

    /**
     * 超级管理员后台查看公益列表，此方法需登录
     * @param request
     * @return
     */
    @RequestMapping("getAllGongyiByMall")
    @ResponseBody
    public List<GmsGongyi> getAllGongyiByMall(HttpServletRequest request){
        String flag = CookieUtil.checkMallAdmin(request);
        if (flag.equals("success")){
            List<GmsGongyi> gongyiList = gongyiService.getAllGongyi();
            return gongyiList;

        }
       return null;
    }



    /**
     * 得到公益项目的详情信息
     * @param  gid
     * @return
     */
    @RequestMapping("getGongyiDetail")
    @ResponseBody
    public GmsGongyi getGongyiDetail(String gid){
        GmsGongyi gmsGongyi=new GmsGongyi();
        gmsGongyi.setGid(gid);
       GmsGongyi gmsGongyi1= gongyiService.getGongyiDetail(gmsGongyi);
        return gmsGongyi1;
    }

    /**
     * 店铺管理员修改本店参加的公益项目
     */
    @RequestMapping("updateGidBySid")
    @ResponseBody
    public String updateGidBySid(String gid,HttpServletRequest request){
        SmsStore smsStore = new SmsStore();
        smsStore.setGid(gid);
        String sid = CookieUtil.checkAdmin(request);
        if (sid.equals("")&&gid.equals("")){
            return "false";
        }
        smsStore.setSid(sid);
        storeService.updateGidBySid(smsStore);
        return "success";
    }

}
