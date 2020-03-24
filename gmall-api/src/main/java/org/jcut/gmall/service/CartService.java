package org.jcut.gmall.service;

import org.jcut.gmall.bean.OmsCart;

import java.util.List;

public interface CartService {
    OmsCart ifCartExistByUser(String uid, String pid);

    void addCart(OmsCart omsCartItem);

    void updateCart(OmsCart omsCartFromDb);

    void flushCartCache(String uid);

    List<OmsCart> cartList(String uid);

    void updateCartNum(int num, String pid);

    void checkCart(OmsCart omsCart);


    void initCart(OmsCart omsCart);

    void delCart(String id,String uid);
}
