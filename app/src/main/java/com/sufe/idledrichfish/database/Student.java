package com.sufe.idledrichfish.database;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class Student extends LitePalSupport {

    @Column(unique = true, nullable = false)
    private String id;  // 学号

    private String name;

    private String password;

    private String gender;

    private float credit;

    private String state;   // 审核状态

    private int admin_id;   // 注册时审核此学号的admin

    private List<Product> favorites = new ArrayList<Product>(); // 收藏夹

    private List<Product> published_product = new ArrayList<Product>();   // 发布的商品

    // generated getters and setters.

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public float getCredit() {
        return credit;
    }

    public void setCredit(float credit) {
        this.credit = credit;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public List<Product> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Product> favorites) {
        this.favorites = favorites;
    }

    public List<Product> getPublished_product() {
        return published_product;
    }

    public void setPublished_product(List<Product> published_product) {
        this.published_product = published_product;
    }
}