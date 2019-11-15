package com.sufe.idledrichfish.database;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class Admin extends LitePalSupport {

    @Column(unique = true, nullable = false)
    private int admin_id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    // generated getters and setters.

    public int getAdminId() {
        return admin_id;
    }

    public void setAdminId(int admin_id) {
        this.admin_id = admin_id;
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
