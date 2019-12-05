package com.sufe.idledrichfish.ui.home;

class HomeStudentView {
    private String sellerId;

    private String sellerName;

    private float sellerCredit;

    private String sellerImage;

    HomeStudentView(String sellerId, String sellerName, float sellerCredit, String sellerImage) {
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.sellerCredit = sellerCredit;
        this.sellerImage = sellerImage;
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

    public String getSellerId() {
        return sellerId;
    }
}
