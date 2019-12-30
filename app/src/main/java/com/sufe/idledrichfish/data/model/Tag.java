package com.sufe.idledrichfish.data.model;

import cn.bmob.v3.BmobObject;

public class Tag extends BmobObject {

    private String name;

    private Product product;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
