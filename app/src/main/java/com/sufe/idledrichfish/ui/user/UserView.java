package com.sufe.idledrichfish.ui.user;

public class UserView {

    private String orderId;

    private int status;

    private String productId;

    private String productName;

    private double price;

    private byte[] productImage;


    public UserView(String orderId, int status, String productId, String productName, double price, byte[] productImage) {
        this.orderId = orderId;
        this.status = status;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.productImage = productImage;
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

}
