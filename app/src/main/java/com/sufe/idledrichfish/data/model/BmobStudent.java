package com.sufe.idledrichfish.data.model;

import com.sufe.idledrichfish.database.Admin;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class BmobStudent extends BmobUser {

    /*
     * ObjectId
     * username --- 学号
     * password
     * email
     * mobilePhoneNumber
     */

    private String name;  // 真实姓名

    private String gender;

    private float credit;

    private BmobFile image;

    private BmobDate lastLoginDate;

    private boolean state;   // 审核状态

    private Admin admin;   // 注册时审核此学号的admin

    private BmobRelation favorites; // 收藏夹


    // generated getters and setters.

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

    public BmobFile getImage() {
        return image;
    }

    public void setImage(BmobFile image) {
        this.image = image;
    }

    public BmobDate getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(BmobDate lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public BmobRelation getFavorites() {
        return favorites;
    }

    public void setFavorites(BmobRelation favorites) {
        this.favorites = favorites;
    }

}