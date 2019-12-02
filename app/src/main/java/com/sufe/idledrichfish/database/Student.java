package com.sufe.idledrichfish.database;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;

public class Student extends LitePalSupport {

    @Column(unique = true, nullable = false)
    private String studentId;  // Bmob生成的Id

    @Column(unique = true, nullable = false)
    private String studentNumber;  // 学号

    @Column(nullable = false)
    private String name;

    private String gender = "外星人";

    private float credit = 10;

    private byte[] image;

    private Date lastRegisterDate;

//    private String state = "verifing";   // 审核状态

//    @Column(nullable = false)
//    private int admin_id;   // 注册时审核此学号的admin

//    private List<Product> favorites = new ArrayList<Product>(); // 收藏夹

//    private List<Product> published_product = new ArrayList<Product>();   // 发布的商品

//    private List<Product> purchased_product = new ArrayList<Product>();   // 买到的商品


    // generated getters and setters.

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return lastRegisterDate;
    }

    public void setLastRegisterDate(Date lastRegisterDate) {
        this.lastRegisterDate = lastRegisterDate;
    }

//    public String getState() {
//        return state;
//    }
//
//    public void setState(String state) {
//        this.state = state;
//    }

//    public int getAdminId() {
//        return admin_id;
//    }
//
//    public void setAdminId(int admin_id) {
//        this.admin_id = admin_id;
//    }

//    public List<Product> getFavorites() {
//        return favorites;
//    }
//
//    public void setFavorites(List<Product> favorites) {
//        this.favorites = favorites;
//    }

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
