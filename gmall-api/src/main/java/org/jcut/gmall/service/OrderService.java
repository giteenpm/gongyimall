package org.jcut.gmall.service;

import org.jcut.gmall.bean.OmsOrder;

import java.util.List;

public interface OrderService {
    void createOrder(OmsOrder omsOrder);

    List<OmsOrder> getOrderDetailByUser(String uid);

    List<OmsOrder> getOrderByAdmin(String sid);

    List<OmsOrder> getOrderNoSend(String sid);

    void updateOrderState(OmsOrder omsOrder);

    void updateOrderStatusZero(OmsOrder omsOrder);


    void sendOrder(String oid);
}
