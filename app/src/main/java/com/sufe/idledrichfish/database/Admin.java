package com.sufe.idledrichfish.database;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class Admin extends LitePalSupport {

    @Column(unique = true, nullable = false)
    private int id;

    private String name;

    private String password;

    // generated getters and setters.

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
