package com.sufe.idledrichfish.data.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class Order extends BmobObject {

    private Student seller;

    private Student buyer;

    private Product product;

    private int status;

    private BmobDate orderDate;


    public Student getSeller() {
        return seller;
    }

    public void setSeller(Student seller) {
        this.seller = seller;
    }

    public Student getBuyer() {
        return buyer;
    }

    public void setBuyer(Student buyer) {
        this.buyer = buyer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BmobDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(BmobDate orderDate) {
        this.orderDate = orderDate;
    }
}
