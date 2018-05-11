package com.lyw.tracker.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by liuyuanwen on 17/4/23.
 */

public class UserInfoBean extends BmobObject {

    private String phone;
    private String pwd;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
