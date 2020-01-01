package com.sufe.idledrichfish.ui.myFavorite;

public class FavoriteProductView {

    String  productId;

    String name;

    double price;

    String sellerId;

    String sellerName;

    float credit;

    byte[] image1;

    byte[] image2;

    byte[] image3;

    byte[] image4;

    byte[] sellerImage;


    public FavoriteProductView(String productId, String name, double price, String sellerId,
                               String sellerName, float credit, byte[] image1, byte[] image2, byte[] image3, byte[] image4, byte[] sellerImage) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.credit = credit;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.sellerImage = sellerImage;
    }


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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public byte[] getSellerImage() {
        return sellerImage;
    }

    public void setSellerImage(byte[] sellerImage) {
        this.sellerImage = sellerImage;
    }
}
