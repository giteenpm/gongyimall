package org.jcut.gmall.interceptors;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.jcut.gmall.annotations.LoginRequired;
import org.jcut.gmall.util.CookieUtil;
import org.jcut.gmall.util.HttpclientUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

@Component
@CrossOrigin
public class AuthInterceptor extends HandlerInterceptorAdapter {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
        response.setHeader("Access-control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", request.getMethod());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
        //防止乱码，适用于传输JSON数据
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        StringBuffer url = request.getRequestURL();
        System.out.println(url);
//        if (httpRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
//            setHeader(httpRequest, httpResponse);
//            return true;
//        }

        //判断需要拦截的方法的携带的注解是否需要被拦截，需使用反射
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        LoginRequired methodAnnotation = handlerMethod.getMethodAnnotation(LoginRequired.class);
        if (methodAnnotation == null) {
                //如果没有此注解，则拦截器放行，不进行处理
                return true;
        }

        String token = "";
        String oldToken = CookieUtil.getCookieValue(request, "oldToken", true);
        if (StringUtils.isNotBlank(oldToken)) {
            token = oldToken;
        }
        String newToken = request.getParameter("token");
        if (StringUtils.isNotBlank(newToken)) {
            token = newToken;
        }
        //拦截器处理这个方法，但是处理成功与否仍要进行判断
        boolean loginSuccess = methodAnnotation.loginSuccess();

        //调用认证中心进行token验证
        String success = "fail";
        Map<String,String> successMap = new HashMap<>();
        if(StringUtils.isNotBlank(token)){
            String ip = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
            if(StringUtils.isBlank(ip)){
                ip = request.getRemoteAddr();// 从request中获取ip
                if(StringUtils.isBlank(ip)){
                    ip = "127.0.0.1";
                }
            }
            String successJson  = HttpclientUtil.doGet("http://localhost:8084/verify?token=" + token+"&currentIp="+ip);

            successMap = JSON.parseObject(successJson,Map.class);

            success = successMap.get("status");

        }



        if (loginSuccess) {
            //必须登录成功才能使用
            if (!success.equals("success")) {
                //重定向回passport登录
//                StringBuffer requestURL = request.getRequestURL();
//
//            response.sendRedirect("http://127.0.0.1:8020/shangcheng/login.html?ReturnUrl="+requestURL);
                return false;
            } else {
                //验证通过，覆盖cookie中的token
                //需要将token携带的用户信息写入
                request.setAttribute("uid", successMap.get("uid"));
                request.setAttribute("username", successMap.get("username"));

                //验证通过，覆盖cookie中的token
                if (StringUtils.isNotBlank(token)) {
                    CookieUtil.setCookie(request, response, "oldToken", token, 60 * 60*2, true);
                }
            }
        } else {
            //没登录也能通过，但必须验证
            if (success.equals("success")) {
                //重定向回passport登录
                request.setAttribute("uid", successMap.get("uid"));
                request.setAttribute("username", successMap.get("username"));

                //验证通过，覆盖cookie中的token
                if (StringUtils.isNotBlank(token)) {
                    CookieUtil.setCookie(request, response, "oldToken", token, 60 * 60 * 60, true);
                }

            }
        }


        return true;
    }

    //设置跨域
    private void setHeader(HttpServletRequest request, HttpServletResponse response) {
        //跨域的header设置
        response.setHeader("Access-control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", request.getMethod());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
        //防止乱码，适用于传输JSON数据
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
    }

}
