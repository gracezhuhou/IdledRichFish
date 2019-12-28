package com.sufe.idledrichfish.data.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

public class Tab extends BmobObject {

    private String name;

    private BmobRelation products;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BmobRelation getProducts() {
        return products;
    }

    public void setProducts(BmobRelation products) {
        this.products = products;
    }
}
