package org.jcut.gmall.order.mapper;

import org.jcut.gmall.bean.OmsOrder;
import org.jcut.gmall.bean.OmsOrderDetail;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OrderDetailMapper extends Mapper<OmsOrderDetail> {
    List<OmsOrder> selectOrderDetailByUser(String uid);
}
