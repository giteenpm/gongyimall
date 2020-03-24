package org.jcut.gmall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.jcut.gmall.bean.PmsProductInfo;
import org.jcut.gmall.service.ProductService;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
public class ItemController {
    //注入已经存在的服务service
    @Reference
    ProductService productService;

    @RequestMapping("getItem")
    @ResponseBody
    public PmsProductInfo getItem(String pid){
       PmsProductInfo pmsProductInfo= productService.getProductOne(pid);
        return pmsProductInfo;
    }
}
