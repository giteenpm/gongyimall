package org.jcut.gmall.gongyi.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.jcut.gmall.bean.GmsNews;
import org.jcut.gmall.service.NewsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@CrossOrigin
public class NewsController {
    @Reference
    NewsService newsService;

    @RequestMapping("editNews")
    @ResponseBody
    public String editNews(GmsNews gmsNews){
        String nid = UUID.randomUUID().toString();
        gmsNews.setNid(nid);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = dateFormat.format(date);
        gmsNews.setTime(format);
        newsService.editNews(gmsNews);
        return  "success";
    }

    /**
     * 得到全部的新闻列表信息
     * @return
     */
    @RequestMapping("getAllNews")
    @ResponseBody
    public List<GmsNews> getAllNews(){
       List<GmsNews> newsList= newsService.getAllNews();
       return newsList;
    }

    /**
     * 根据新闻资讯id去查询指定新闻资讯
     * @param nid
     * @return
     */
    @RequestMapping("getNewsDetail")
    @ResponseBody
    public GmsNews getNewsDetail(String nid){

       GmsNews gmsNews= newsService.getNewsDetail(nid);
       return  gmsNews;
    }
}
