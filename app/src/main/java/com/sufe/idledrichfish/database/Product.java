package com.sufe.idledrichfish.database;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Product extends LitePalSupport {

    @Column(unique = true, nullable = false)
    private String productId;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private double price;

    private double oldPrice;

    private boolean isNew; // 是否全新

    private boolean canBargain; // 能否讲价

    private Date publishDate = new Date(System.currentTimeMillis());

    @Column(nullable = false)
    private String publisherId;    // 发布者的学号

    private String category = "other";

    private List<Label> labels = new ArrayList<Label>();

    private byte[] image1;

    private byte[] image2;

    private byte[] image3;

    private byte[] image4;

    private byte[] image5;

    private byte[] image6;

    private byte[] image7;

    private byte[] image8;

    private byte[] image9;


    // generated getters and setters.

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

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

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public byte[] getImage1() {
        return image1;
    }

    public void setImage1(byte[] image1) {
        this.image1 = image1;
    }

    public byte[] getImage2() {
        return image2;
    }

    public void setImage2(byte[] image2) {
        this.image2 = image2;
    }

    public byte[] getImage3() {
        return image3;
    }

    public void setImage3(byte[] image3) {
        this.image3 = image3;
    }

    public byte[] getImage4() {
        return image4;
    }

    public void setImage4(byte[] image4) {
        this.image4 = image4;
    }

    public byte[] getImage5() {
        return image5;
    }

    public void setImage5(byte[] image5) {
        this.image5 = image5;
    }

    public byte[] getImage6() {
        return image6;
    }

    public void setImage6(byte[] image6) {
        this.image6 = image6;
    }

    public byte[] getImage7() {
        return image7;
    }

    public void setImage7(byte[] image7) {
        this.image7 = image7;
    }

    public byte[] getImage8() {
        return image8;
    }

    public void setImage8(byte[] image8) {
        this.image8 = image8;
    }

    public byte[] getImage9() {
        return image9;
    }

    public void setImage9(byte[] image9) {
        this.image9 = image9;
    }
}
