package org.jcut.gmall.manage.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import org.jcut.gmall.bean.PmsBrand;
import org.jcut.gmall.service.BrandService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
/*
    茶叶品牌controller
 */
@Controller
@CrossOrigin
public class BrandController {
    @Reference
    BrandService brandService;
    @RequestMapping("getAllBrand")
    @ResponseBody
    public List<PmsBrand> getAllBrand(){
        List<PmsBrand> pmsBrandList=brandService.getAllBrand();
        return pmsBrandList;
    }
}
