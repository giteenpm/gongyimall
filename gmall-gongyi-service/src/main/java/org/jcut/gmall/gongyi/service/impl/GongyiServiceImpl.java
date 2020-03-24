package org.jcut.gmall.gongyi.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.jcut.gmall.bean.GmsGongyi;
import org.jcut.gmall.bean.OmsOrder;
import org.jcut.gmall.gongyi.mapper.GongyiMapper;
import org.jcut.gmall.service.GongyiService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class GongyiServiceImpl implements GongyiService {
    @Autowired
    GongyiMapper gongyiMapper;
    @Override
    public void publishGongyi(GmsGongyi gmsGongyi) {
       gongyiMapper.insertSelective(gmsGongyi);
    }

    @Override
    public List<GmsGongyi> getAllGongyi() {
        List<GmsGongyi> gongyiList = gongyiMapper.selectAll();
        return gongyiList;
    }

    @Override
    public GmsGongyi getGongyiDetail(GmsGongyi gmsGongyi) {
        GmsGongyi gmsGongyi1 = gongyiMapper.selectOne(gmsGongyi);
        return gmsGongyi1;
    }

    @Override
    public void updateGongyiItem(String gid, Double money) {
        GmsGongyi gmsGongyi=new GmsGongyi();
        gmsGongyi.setGid(gid);
        GmsGongyi gmsGongyi1 = gongyiMapper.selectOne(gmsGongyi);

        GmsGongyi gmsGongyi2=new GmsGongyi();

        gmsGongyi2.setNum(gmsGongyi1.getNum()+1);
        gmsGongyi2.setReceive_money(gmsGongyi1.getReceive_money()+money*0.1);
        Example example=new Example(GmsGongyi.class);
        example.createCriteria().andEqualTo("gid",gid);
        gongyiMapper.updateByExampleSelective(gmsGongyi2,example);
    }
}
