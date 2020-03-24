package org.jcut.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import org.jcut.gmall.bean.PmsCatalog;
import org.jcut.gmall.manage.mapper.CataLogMapper;
import org.jcut.gmall.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;
@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    CataLogMapper cataLogMapper;
    @Override
    public List<PmsCatalog> getAllCatalog() {
       List<PmsCatalog> catalogs= cataLogMapper.selectAll();
        return catalogs;
    }
}
