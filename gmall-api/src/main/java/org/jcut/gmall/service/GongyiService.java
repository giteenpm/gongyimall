package org.jcut.gmall.service;

import org.jcut.gmall.bean.GmsGongyi;

import java.util.List;

public interface GongyiService {
    void publishGongyi(GmsGongyi gmsGongyi);

    List<GmsGongyi> getAllGongyi();

    GmsGongyi getGongyiDetail(GmsGongyi gmsGongyi);

    void updateGongyiItem(String gid, Double money);
}
