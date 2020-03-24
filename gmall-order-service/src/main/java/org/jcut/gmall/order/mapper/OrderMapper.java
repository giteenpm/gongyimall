package org.jcut.gmall.order.mapper;

import org.jcut.gmall.bean.OmsOrder;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OrderMapper extends Mapper<OmsOrder> {
    List<OmsOrder> selectOrderDetailByUser(String uid);

    List<OmsOrder> selectByUid(OmsOrder t);
}
