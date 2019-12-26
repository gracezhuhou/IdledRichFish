package com.sufe.idledrichfish.data;

import java.util.List;

import cn.bmob.v3.datatype.BmobRelation;

public class ProductRepository {

    private static volatile ProductRepository instance;

    private ProductDataSource dataSource;

    // private constructor : singleton access
    private ProductRepository(ProductDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static ProductRepository getInstance(ProductDataSource dataSource) {
        if (instance == null) {
            instance = new ProductRepository(dataSource);
        }
        return instance;
    }

    public void deleteProduct(String objectId) {
        dataSource.deleteProduct(objectId);
    }

    public void saveProduct(String productName, String description, boolean isNew, boolean canBargain,
                            double price, double oldPrice, BmobRelation labels, String category,
                            List<String> imagePath) {
        dataSource.saveProduct(productName, description, isNew, canBargain, price, oldPrice,
                labels, category, imagePath);
    }

    public void queryProduct(String id, String type){
        if (type.equals("productInfo")){
            dataSource.queryProductAllInfo(id);
        } else if (type.equals("chat")) {
            dataSource.queryProductForChat(id);
        }
    }

    public void queryProductsForHome(Boolean policy) {
        dataSource.queryProductsForHome(policy);
    }

    public void queryMyPublishProducts() {
        dataSource.queryMyPublishProducts();
    }
}
