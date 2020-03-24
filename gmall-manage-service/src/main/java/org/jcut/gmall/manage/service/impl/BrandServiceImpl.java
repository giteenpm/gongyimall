package org.jcut.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.jcut.gmall.bean.PmsBrand;
import org.jcut.gmall.manage.mapper.BrandMapper;
import org.jcut.gmall.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    BrandMapper brandMapper;
    @Override
    public List<PmsBrand> getAllBrand() {
        List<PmsBrand> pmsBrandList = brandMapper.selectAll();
        return pmsBrandList;
    }
}
