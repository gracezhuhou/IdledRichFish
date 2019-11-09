package com.sufe.idledrichfish.database;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class Label extends LitePalSupport {

    @Column(unique = true, nullable = false)
    private int id;

    private String name;

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
}
