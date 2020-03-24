package org.jcut.gmall.manage.mapper;


import org.jcut.gmall.bean.PmsProductInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ProductMapper extends Mapper<PmsProductInfo> {
    List<PmsProductInfo> selectProductRandom();
}
