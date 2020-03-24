package org.jcut.gmall.bean;

import java.io.Serializable;

public class SmsStore implements Serializable{
    private String sid;
    private String sname;
    private String admin_name;
    private String admin_pwd;
    private String admin_email;
    private String company_name;
    private String company_phone;
    private String company_email;
    private String detail_address;
    private String license_num;
    private String tax_num;
    private String rep_people;
    private String rep_idcard;
    private String account_id;
    private String account_name;
    private String gid;
    private String store_status;

    public String getStore_status() {
        return store_status;
    }

    public void setStore_status(String store_status) {
        this.store_status = store_status;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public String getAdmin_pwd() {
        return admin_pwd;
    }

    public void setAdmin_pwd(String admin_pwd) {
        this.admin_pwd = admin_pwd;
    }

    public String getAdmin_email() {
        return admin_email;
    }

    public void setAdmin_email(String admin_email) {
        this.admin_email = admin_email;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_phone() {
        return company_phone;
    }

    public void setCompany_phone(String company_phone) {
        this.company_phone = company_phone;
    }

    public String getCompany_email() {
        return company_email;
    }

    public void setCompany_email(String company_email) {
        this.company_email = company_email;
    }

    public String getDetail_address() {
        return detail_address;
    }

    public void setDetail_address(String detail_address) {
        this.detail_address = detail_address;
    }

    public String getLicense_num() {
        return license_num;
    }

    public void setLicense_num(String license_num) {
        this.license_num = license_num;
    }

    public String getTax_num() {
        return tax_num;
    }

    public void setTax_num(String tax_num) {
        this.tax_num = tax_num;
    }

    public String getRep_people() {
        return rep_people;
    }

    public void setRep_people(String rep_people) {
        this.rep_people = rep_people;
    }

    public String getRep_idcard() {
        return rep_idcard;
    }

    public void setRep_idcard(String rep_idcard) {
        this.rep_idcard = rep_idcard;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }
}
