package org.jcut.gmall.passport.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.jcut.gmall.bean.UmsUser;
import org.jcut.gmall.service.UserService;
import org.jcut.gmall.util.JwtUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin
public class PassportController {
    @Reference
    UserService userService;

    @RequestMapping("login")
    @ResponseBody
    public String login(UmsUser umsUser, HttpServletRequest request){
        String token="";
        UmsUser umsUser1=userService.login(umsUser);
        if (umsUser1!=null){
            String uid = umsUser1.getUid();
            String username = umsUser1.getUsername();
            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("uid",uid);
            userMap.put("username",username);

            //制作盐值部分
            String ip=request.getHeader("x-forwarded-for");
            if (StringUtils.isBlank(ip)){
                ip=request.getRemoteAddr();
                if (StringUtils.isBlank(ip)){
                    ip="127.0.0.1";
                }
            }
            //按照设计的算法进行加密。因为只是为了演示业务，所以此模块过于简单哈哈
             token = JwtUtil.encode("gongyimall", userMap, ip);
            userService.addUserToken(token,uid);
        }else {
            token="fail";
        }
        return token;
    }

    @RequestMapping("verify")
    @ResponseBody
    public String verify(String token,String currentIp){
        // 通过jwt校验token真假
        Map<String,String> map = new HashMap<>();

        Map<String, Object> decode = JwtUtil.decode(token, "gongyimall", currentIp);

        if(decode!=null){
            map.put("status","success");
            map.put("uid",(String)decode.get("uid"));
            map.put("username",(String)decode.get("username"));
        }else{
            map.put("status","fail");
        }


        return JSON.toJSONString(map);

    }
}
