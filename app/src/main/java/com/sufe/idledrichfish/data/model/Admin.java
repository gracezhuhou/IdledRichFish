package com.sufe.idledrichfish.data.model;

import cn.bmob.v3.BmobObject;

public class Admin extends BmobObject {

    private String name;

    private String password;


    // generated getters and setters.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
