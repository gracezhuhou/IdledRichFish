package com.sufe.idledrichfish.database;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Product extends LitePalSupport {

    @Column(unique = true, nullable = false)
    private String product_id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private double price;

    private Date publish_date = new Date(System.currentTimeMillis());

    @Column(nullable = false)
    private String publisher_id;    // 发布者的学号

    private String category = "other";

    private List<Label> labels = new ArrayList<Label>();

    private byte[] image; // 图片

    // generated getters and setters.

    public String getProductId() {
        return product_id;
    }

    public void setProductId(String product_id) {
        this.product_id = product_id;
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

    public Date getPublishDate() {
        return publish_date;
    }

    public void setPublishDate(Date publish_date) {
        this.publish_date = publish_date;
    }

    public String getPublisherId() {
        return publisher_id;
    }

    public void setPublisherId(String publisher_id) {
        this.publisher_id = publisher_id;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}