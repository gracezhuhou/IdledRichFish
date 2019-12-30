package com.sufe.idledrichfish.data.model;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobRelation;

public class Product extends BmobObject{

    private String name;

    private String description;

    private double price;

    private double oldPrice; // 入手价

    private boolean isNew; // 是否全新

    private boolean canBargain; // 能否讲价

    private BmobDate publishDate;

    private Student seller;    // 发布者

    private String category;

//    private BmobRelation tabs;

    private List<Byte> image1;

    private List<Byte> image2;

    private List<Byte> image3;

    private List<Byte> image4;

    private List<Byte> image5;

    private List<Byte> image6;

    private List<Byte> image7;

    private List<Byte> image8;

    private List<Byte> image9;


    // generated getters and setters.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isCanBargain() {
        return canBargain;
    }

    public void setCanBargain(boolean canBargain) {
        this.canBargain = canBargain;
    }

    public Student getSeller() {
        return seller;
    }

    public void setSeller(Student seller) {
        this.seller = seller;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BmobDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(BmobDate publishDate) {
        this.publishDate = publishDate;
    }

//    public BmobRelation getTabs() {
//        return tabs;
//    }
//
//    public void setTabs(BmobRelation tabs) {
//        this.tabs = tabs;
//    }

    public List<Byte> getImage1() {
        return image1;
    }

    public void setImage1(List<Byte> image1) {
        this.image1 = image1;
    }

    public List<Byte> getImage2() {
        return image2;
    }

    public void setImage2(List<Byte> image2) {
        this.image2 = image2;
    }

    public List<Byte> getImage3() {
        return image3;
    }

    public void setImage3(List<Byte> image3) {
        this.image3 = image3;
    }

    public List<Byte> getImage4() {
        return image4;
    }

    public void setImage4(List<Byte> image4) {
        this.image4 = image4;
    }

    public List<Byte> getImage5() {
        return image5;
    }

    public void setImage5(List<Byte> image5) {
        this.image5 = image5;
    }

    public List<Byte> getImage6() {
        return image6;
    }

    public void setImage6(List<Byte> image6) {
        this.image6 = image6;
    }

    public List<Byte> getImage7() {
        return image7;
    }

    public void setImage7(List<Byte> image7) {
        this.image7 = image7;
    }

    public List<Byte> getImage8() {
        return image8;
    }

    public void setImage8(List<Byte> image8) {
        this.image8 = image8;
    }

    public List<Byte> getImage9() {
        return image9;
    }

    public void setImage9(List<Byte> image9) {
        this.image9 = image9;
    }
}
