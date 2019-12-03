package com.sufe.idledrichfish.ui.home;

/**
 * 首页商品UI需要的数据
 */
class HomeProductView {
    private String objectId;

    private String name;

    private double price;

    private boolean isNew;

    private boolean canBargain;

    private String sellerName;

    private float sellerCredit;

    private String sellerImage;

    private String productImage;

    HomeProductView(String objectId, String name, double price, boolean isNew, boolean canBargain,
                    String sellerName, float sellerCredit, String sellerImage, String productImage) {
        this.objectId = objectId;
        this.name = name;
        this.price = price;
        this.isNew = isNew;
        this.canBargain = canBargain;
        this.sellerName = sellerName;
        this.sellerCredit = sellerCredit;
        this.sellerImage = sellerImage;
        this.productImage = productImage;
    }

    String getObjectId() {
        return objectId;
    }

    String getName() {
        return name;
    }

    double getPrice() {
        return price;
    }

    boolean isNew() {
        return isNew;
    }

    boolean isCanBargain() {
        return canBargain;
    }

    String getSellerName() {
        return sellerName;
    }

    float getSellerCredit() {
        return sellerCredit;
    }

    String getSellerImage() {
        return sellerImage;
    }

    String getProductImage() {
        return productImage;
    }
}
