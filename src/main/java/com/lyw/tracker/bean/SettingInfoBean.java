package com.lyw.tracker.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by liuyuanwen on 17/4/24.
 */

public class SettingInfoBean extends BmobObject {

    private String Address;
    private String Name;
    private String Type;
    private String longitude;//经度
    private String latitude;//纬度
    private String phone;

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
