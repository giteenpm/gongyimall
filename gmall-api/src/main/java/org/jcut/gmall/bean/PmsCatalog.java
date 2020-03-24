package org.jcut.gmall.bean;

import java.io.Serializable;

public class PmsCatalog implements Serializable {
   private String cid;
   private String name;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
