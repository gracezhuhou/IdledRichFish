package com.sufe.idledrichfish.database;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;

public class Order extends LitePalSupport {

    @Column(unique = true, nullable = false)
    private int id;

    private String state;   // 订单状态

    private Date date;  // 下单日期

    // generated getters and setters.


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
