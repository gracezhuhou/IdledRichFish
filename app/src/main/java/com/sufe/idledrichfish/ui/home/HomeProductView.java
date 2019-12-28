package com.sufe.idledrichfish.ui.home;

public class HomeProductView {

    private String objectId;

    private String name;

    private double price;

    private boolean isNew; // 是否全新

    private boolean canBargain; // 能否讲价

    private byte[] image1;

    // 卖家信息
    private String sellerId;

    private String sellerName;

    private float credit;

    private byte[] sellerImage;


    public HomeProductView(String objectId, String name, double price, boolean isNew,
                           boolean canBargain, byte[] image1, String sellerId,
                           String sellerName, float credit, byte[] sellerImage) {
        this.objectId = objectId;
        this.name = name;
        this.price = price;
        this.isNew = isNew;
        this.canBargain = canBargain;
        this.image1 = image1;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.credit = credit;
        this.sellerImage = sellerImage;
    }

    // generated getters and setters.

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public byte[] getImage1() {
        return image1;
    }

    public void setImage1(byte[] image1) {
        this.image1 = image1;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public float getCredit() {
        return credit;
    }

    public void setCredit(float credit) {
        this.credit = credit;
    }

    public byte[] getSellerImage() {
        return sellerImage;
    }

    public void setSellerImage(byte[] sellerImage) {
        this.sellerImage = sellerImage;
    }
}
