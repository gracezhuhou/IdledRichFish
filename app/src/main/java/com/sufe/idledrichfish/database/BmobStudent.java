package com.sufe.idledrichfish.database;

import org.litepal.annotation.Column;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class BmobStudent extends BmobUser {

    /*
     * ObjectId
     * username
     * password
     * email
     * mobilePhoneNumber
     */

    @Column(unique = true, nullable = false)
    private String studentNumber;  // 学号,不要和Student类的studentId搞混

    private String gender = "外星人";

    private float credit = 10;

    private byte[] image;

    private Date last_register_date;

    private String state = "verifing";   // 审核状态

    @Column(nullable = false)
    private int admin_id;   // 注册时审核此学号的admin

    private List<Product> favorites = new ArrayList<Product>(); // 收藏夹

//    private List<Product> published_product = new ArrayList<Product>();   // 发布的商品

//    private List<Product> purchased_product = new ArrayList<Product>();   // 买到的商品


    // generated getters and setters.

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Date getLastRegisterDate() {
        return last_register_date;
    }

    public void setLastRegisterDate(Date last_register_date) {
        this.last_register_date = last_register_date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getAdminId() {
        return admin_id;
    }

    public void setAdminId(int admin_id) {
        this.admin_id = admin_id;
    }

    public List<Product> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Product> favorites) {
        this.favorites = favorites;
    }

//    public List<Product> getPublishedProduct() {
//        return published_product;
//    }
//
//    public void setPublishedProduct(List<Product> published_product) {
//        this.published_product = published_product;
//    }
//
//    public List<Product> getPurchasedProduct() {
//        return purchased_product;
//    }
//
//    public void setPurchasedProduct(List<Product> purchased_product) {
//        this.purchased_product = purchased_product;
//    }
}