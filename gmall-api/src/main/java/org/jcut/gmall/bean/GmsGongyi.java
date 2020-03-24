package org.jcut.gmall.bean;

import java.io.Serializable;

public class GmsGongyi implements Serializable {
    private String gid;
    private String gname;
    private String gimg1;
    private String gimg2;
    private String introduce;
    private String content;
    private String create_time;
    private Double expected_money;
    private Integer num;
    private Double receive_money;
    private String agency;


    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getGimg1() {
        return gimg1;
    }

    public void setGimg1(String gimg1) {
        this.gimg1 = gimg1;
    }

    public String getGimg2() {
        return gimg2;
    }

    public void setGimg2(String gimg2) {
        this.gimg2 = gimg2;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Double getExpected_money() {
        return expected_money;
    }

    public void setExpected_money(Double expected_money) {
        this.expected_money = expected_money;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }


    public Double getReceive_money() {
        return receive_money;
    }

    public void setReceive_money(Double receive_money) {
        this.receive_money = receive_money;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }


}
