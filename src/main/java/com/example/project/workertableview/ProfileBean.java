package com.example.project.workertableview;

public class ProfileBean {
    String wname;
    String address;
    String mobile;
    String splz;

    public ProfileBean(String wname, String address, String mobile, String splz) {
        this.wname = wname;
        this.splz = splz;
        this.mobile = mobile;
        this.address = address;
    }

    public ProfileBean() {
    }

    public String getWname() {
        return wname;
    }

    public String getSplz() {
        return splz;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setWname(String wname) {
        this.wname = wname;
    }

    public void setSplz(String splz) {
        this.splz = splz;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
