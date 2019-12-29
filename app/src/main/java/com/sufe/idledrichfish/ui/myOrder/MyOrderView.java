package com.sufe.idledrichfish.ui.myOrder;

public class MyOrderView {

    String orderId;

    int status;

    String productId;

    String productName;

    double price;

    byte[] productImage;

    String sellerId;

    String sellerName;

    byte[] sellerImage;


    public MyOrderView(String orderId, int status, String productId, String productName, double price, byte[] productImage, String sellerId, String sellerName, byte[] sellerImage) {
        this.orderId = orderId;
        this.status = status;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.productImage = productImage;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.sellerImage = sellerImage;
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public byte[] getProductImage() {
        return productImage;
    }

    public void setProductImage(byte[] productImage) {
        this.productImage = productImage;
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

    public byte[] getSellerImage() {
        return sellerImage;
    }

    public void setSellerImage(byte[] sellerImage) {
        this.sellerImage = sellerImage;
    }
}
