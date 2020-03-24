package org.jcut.gmall.service;

import org.jcut.gmall.bean.GmsNews;

import java.util.List;

public interface NewsService {
    void editNews(GmsNews gmsNews);

    List<GmsNews> getAllNews();

    GmsNews getNewsDetail(String nid);
}
