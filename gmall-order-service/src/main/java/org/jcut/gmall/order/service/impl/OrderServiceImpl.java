package org.jcut.gmall.order.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import org.jcut.gmall.bean.OmsOrder;
import org.jcut.gmall.bean.OmsOrderDetail;
import org.jcut.gmall.order.mapper.OrderDetailMapper;
import org.jcut.gmall.order.mapper.OrderMapper;
import org.jcut.gmall.service.CartService;
import org.jcut.gmall.service.GongyiService;
import org.jcut.gmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    //为了避免耦合，使用远程注入CartService的方式
    @Reference
    CartService cartService;
    @Reference
    GongyiService gongyiService;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderDetailMapper orderDetailMapper;
    @Override
    public void createOrder(OmsOrder omsOrder) {
        //插入订单表
        orderMapper.insertSelective(omsOrder);
        //插入订单详情表
        List<OmsOrderDetail> omsOrderItems = omsOrder.getOmsOrderItems();
        for (OmsOrderDetail omsOrderItem : omsOrderItems) {
            orderDetailMapper.insertSelective(omsOrderItem);
            //删除购物车数据,通过购物车主键进行删除
            cartService.delCart(omsOrderItem.getPid(),omsOrder.getUid());
        }
    }

    @Override
    public List<OmsOrder> getOrderDetailByUser(String uid) {
       List<OmsOrder> omsOrderList= orderDetailMapper.selectOrderDetailByUser(uid);
        return omsOrderList;
    }

    @Override
    public List<OmsOrder> getOrderByAdmin(String sid) {
        OmsOrder omsOrder=new OmsOrder();
        omsOrder.setSid(sid);
        List<OmsOrder> omsOrderList = orderMapper.select(omsOrder);
        return omsOrderList;
    }

    @Override
    public List<OmsOrder> getOrderNoSend(String sid) {
        OmsOrder omsOrder=new OmsOrder();
        omsOrder.setSid(sid);
        omsOrder.setOrder_status("1");
        List<OmsOrder> omsOrderList = orderMapper.select(omsOrder);
        return omsOrderList;
    }

    //支付成功以后，修改订单状态为已支付。并且这些订单中查询是否有参与公益项目，如果有，对应订单金额需要改变  
    @Override
    public void updateOrderState(OmsOrder omsOrder) {
        //查询出订单状态为5的订单
        OmsOrder t=new OmsOrder();
        t.setUid(omsOrder.getUid());
        t.setOrder_status("5");
        //根据要求查询出订单列表以及该订单参加的公益项目id
      List<OmsOrder> omsOrderList= orderMapper.selectByUid(t);
        for (OmsOrder order : omsOrderList) {
            //将封装好的订单信息去远程调用公益项目服务，完成更新
            gongyiService.updateGongyiItem(order.getSmsStore().getGid(),order.getMoney());

        }

        omsOrder.setOrder_status("1");
        Example example=new Example(OmsOrder.class);
        example.createCriteria().andEqualTo("order_status","5");
        orderMapper.updateByExampleSelective(omsOrder,example);
    }

    @Override
    public void updateOrderStatusZero(OmsOrder omsOrder) {
        omsOrder.setOrder_status("0");
        Example example=new Example(OmsOrder.class);
        example.createCriteria().andEqualTo("order_status","5");
        orderMapper.updateByExampleSelective(omsOrder,example);
    }

    @Override
    public void sendOrder(String oid) {
        OmsOrder omsOrder=new OmsOrder();
        omsOrder.setOrder_status("2");
        Example example=new Example(OmsOrder.class);
        example.createCriteria().andEqualTo("oid",oid);
        orderMapper.updateByExampleSelective(omsOrder,example);
    }


}
