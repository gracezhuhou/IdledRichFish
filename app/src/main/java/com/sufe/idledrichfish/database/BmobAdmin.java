package com.sufe.idledrichfish.database;

import org.litepal.annotation.Column;

import cn.bmob.v3.BmobObject;

public class BmobAdmin extends BmobObject {

    @Column(unique = true, nullable = false)
    private int adminId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;


    // generated getters and setters.

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

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
