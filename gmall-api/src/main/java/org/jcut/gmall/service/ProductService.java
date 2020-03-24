package org.jcut.gmall.service;


import org.jcut.gmall.bean.PmsProductInfo;

import java.util.List;

public interface ProductService {
    List<PmsProductInfo> getAllProduct();

    void saveProduct(PmsProductInfo pmsProductInfo);


    PmsProductInfo getProductOne(String pid);

    List<PmsProductInfo> getProductRandom();

    List<PmsProductInfo> getProductBySid(String sid);
}
