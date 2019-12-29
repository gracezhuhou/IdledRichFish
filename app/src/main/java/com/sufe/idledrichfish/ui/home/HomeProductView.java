package com.sufe.idledrichfish.ui.home;

class HomeProductView {

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


    HomeProductView(String objectId, String name, double price, boolean isNew,
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

    String getObjectId() {
        return objectId;
    }

    void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    double getPrice() {
        return price;
    }

    void setPrice(double price) {
        this.price = price;
    }

    boolean isNew() {
        return isNew;
    }

    void setNew(boolean aNew) {
        isNew = aNew;
    }

    boolean isCanBargain() {
        return canBargain;
    }

    void setCanBargain(boolean canBargain) {
        this.canBargain = canBargain;
    }

    byte[] getImage1() {
        return image1;
    }

    void setImage1(byte[] image1) {
        this.image1 = image1;
    }

    String getSellerId() {
        return sellerId;
    }

    void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    String getSellerName() {
        return sellerName;
    }

    void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    float getCredit() {
        return credit;
    }

    void setCredit(float credit) {
        this.credit = credit;
    }

    byte[] getSellerImage() {
        return sellerImage;
    }

    void setSellerImage(byte[] sellerImage) {
        this.sellerImage = sellerImage;
    }
}
