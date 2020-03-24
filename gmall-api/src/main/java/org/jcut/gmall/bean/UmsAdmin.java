package org.jcut.gmall.bean;

import java.io.Serializable;

public class UmsAdmin implements Serializable {
    private String aid;
    private String aname;
    private String pwd;

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getAname() {
        return aname;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
