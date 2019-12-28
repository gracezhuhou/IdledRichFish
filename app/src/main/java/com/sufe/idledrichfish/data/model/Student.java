package com.sufe.idledrichfish.data.model;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class Student extends BmobUser {

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

    private List<Byte> image;

    private BmobDate lastLoginDate;

    private boolean state;   // 审核状态

    private Admin admin;   // 注册时审核此学号的admin

    private BmobRelation history; // 浏览历史记录

    private BmobDate historyDate; // 历史记录里的商品的浏览时间


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

    public List<Byte> getImage() {
        return image;
    }

    public void setImage(List<Byte> image) {
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

    public BmobRelation getHistory() {
        return history;
    }

    public void setHistory(BmobRelation history) {
        this.history = history;
    }

    public BmobDate getHistoryDate() {
        return historyDate;
    }

    public void setHistoryDate(BmobDate historyDate) {
        this.historyDate = historyDate;
    }
}