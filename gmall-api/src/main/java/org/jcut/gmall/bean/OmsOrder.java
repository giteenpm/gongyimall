package org.jcut.gmall.bean;


import java.io.Serializable;
import java.util.List;

public class OmsOrder implements Serializable{
    private String oid;
    private String uid;
    private String sid;
    private Double money;
    private String create_time;
    private String pay_time;
    private String send_time;
    private String over_time;
    private String receive_name;
    private String receive_phone;
    private String post_code;
    private String province;
    private String city;
    private String region;
    private String detail_address;
    //物流单号
    private String logistics_num;
    //订单状态
    private String order_status;
    List<OmsOrderDetail> omsOrderItems;
    private SmsStore smsStore;

    public SmsStore getSmsStore() {
        return smsStore;
    }

    public void setSmsStore(SmsStore smsStore) {
        this.smsStore = smsStore;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getOver_time() {
        return over_time;
    }

    public void setOver_time(String over_time) {
        this.over_time = over_time;
    }

    public String getReceive_name() {
        return receive_name;
    }

    public void setReceive_name(String receive_name) {
        this.receive_name = receive_name;
    }

    public String getReceive_phone() {
        return receive_phone;
    }

    public void setReceive_phone(String receive_phone) {
        this.receive_phone = receive_phone;
    }

    public String getPost_code() {
        return post_code;
    }

    public void setPost_code(String post_code) {
        this.post_code = post_code;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDetail_address() {
        return detail_address;
    }

    public void setDetail_address(String detail_address) {
        this.detail_address = detail_address;
    }

    public String getLogistics_num() {
        return logistics_num;
    }

    public void setLogistics_num(String logistics_num) {
        this.logistics_num = logistics_num;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public List<OmsOrderDetail> getOmsOrderItems() {
        return omsOrderItems;
    }

    public void setOmsOrderItems(List<OmsOrderDetail> omsOrderItems) {
        this.omsOrderItems = omsOrderItems;
    }
}
