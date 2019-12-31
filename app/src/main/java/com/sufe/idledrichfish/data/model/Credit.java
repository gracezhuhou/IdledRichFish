package com.sufe.idledrichfish.data.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class Credit extends BmobObject {

    private int score;

    private BmobDate date;

    private Student seller;

    private Student buyer;

    private Order order;


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public BmobDate getDate() {
        return date;
    }

    public void setDate(BmobDate date) {
        this.date = date;
    }

    public Student getSeller() {
        return seller;
    }

    public void setSeller(Student seller) {
        this.seller = seller;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Student getBuyer() {
        return buyer;
    }

    public void setBuyer(Student buyer) {
        this.buyer = buyer;
    }
}
