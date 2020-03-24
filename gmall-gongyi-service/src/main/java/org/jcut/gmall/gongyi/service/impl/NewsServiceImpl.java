package org.jcut.gmall.gongyi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.jcut.gmall.bean.GmsNews;
import org.jcut.gmall.gongyi.mapper.NewsMapper;
import org.jcut.gmall.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {
    @Override
    public GmsNews getNewsDetail(String nid) {
        GmsNews gmsNews = new GmsNews();
        gmsNews.setNid(nid);
        GmsNews news = newsMapper.selectOne(gmsNews);

        return news;
    }

    @Autowired
    NewsMapper newsMapper;
    @Override
    public void editNews(GmsNews gmsNews) {
        newsMapper.insertSelective(gmsNews);
    }

    @Override
    public List<GmsNews> getAllNews() {
        List<GmsNews> newsList = newsMapper.selectAll();

        return newsList;
    }
}
