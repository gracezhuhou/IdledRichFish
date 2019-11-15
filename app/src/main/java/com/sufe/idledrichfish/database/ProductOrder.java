package com.sufe.idledrichfish.database;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;

public class ProductOrder extends LitePalSupport {

    @Column(unique = true, nullable = false)
    private int order_id;

    private String state = "published";   // 订单状态

    private Date date = new Date(System.currentTimeMillis());  // 下单日期

    // generated getters and setters.

    public int getOrderId() {
        return order_id;
    }

    public void setOrderId(int order_id) {
        this.order_id = order_id;
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
