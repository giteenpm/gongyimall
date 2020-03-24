package org.jcut.gmall.bean;

import java.io.Serializable;
import java.util.Date;

public class PmsProductInfo implements Serializable {
    private String pid;
    private String sid;
    private String bid;
    private String cid;
    private String pname;
    private String pdesc;
    private String pimg;
    private Double mprice;
    private Double lprice;
    private Integer num;
    private String create_time;
    private Integer status;
    private String packing;
    private String level;
    private String pdate;
    private Integer save_time;
    private Integer weight;

    public Integer getSale_num() {
        return sale_num;
    }

    public void setSale_num(Integer sale_num) {
        this.sale_num = sale_num;
    }

    private Integer sale_num;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPdesc() {
        return pdesc;
    }

    public void setPdesc(String pdesc) {
        this.pdesc = pdesc;
    }

    public String getPimg() {
        return pimg;
    }

    public void setPimg(String pimg) {
        this.pimg = pimg;
    }

    public Double getMprice() {
        return mprice;
    }

    public void setMprice(Double mprice) {
        this.mprice = mprice;
    }

    public Double getLprice() {
        return lprice;
    }

    public void setLprice(Double lprice) {
        this.lprice = lprice;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPacking() {
        return packing;
    }

    public void setPacking(String packing) {
        this.packing = packing;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPdate() {
        return pdate;
    }

    public void setPdate(String pdate) {
        this.pdate = pdate;
    }

    public Integer getSave_time() {
        return save_time;
    }

    public void setSave_time(Integer save_time) {
        this.save_time = save_time;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
