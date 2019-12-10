package com.sufe.idledrichfish.data.model;

import cn.bmob.v3.BmobObject;

public class Favorite extends BmobObject {

    private Student student;

    private Product product;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
