package org.jcut.gmall.bean;

import java.io.Serializable;

public class OmsOrderDetail implements Serializable {
    private String id;
    private String oid;
    private String pid;
    private String pdesc;
    //商品单价
    private Double price;
    private Integer num;
    private String img;
    private OmsOrder omsOrder;

    public OmsOrder getOmsOrder() {
        return omsOrder;
    }

    public void setOmsOrder(OmsOrder omsOrder) {
        this.omsOrder = omsOrder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPdesc() {
        return pdesc;
    }

    public void setPdesc(String pdesc) {
        this.pdesc = pdesc;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
