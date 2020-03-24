package org.jcut.gmall.bean;

import java.io.Serializable;

public class PmsBrand implements Serializable {
    private String bid;
    private String bname;

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }
}
