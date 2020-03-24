package org.jcut.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.jcut.gmall.bean.PmsCatalog;
import org.jcut.gmall.service.CatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
@Controller
@CrossOrigin
public class CatalogController {
    @Reference
    CatalogService catalogService;
    @RequestMapping("getAllCatalog")
    @ResponseBody
    public List<PmsCatalog> getAllCatalog(){
       List<PmsCatalog> catalogList= catalogService.getAllCatalog();
       return catalogList;
    }
}
