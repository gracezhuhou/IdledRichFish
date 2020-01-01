package com.sufe.idledrichfish.data;

import java.util.List;

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
                            double price, double oldPrice, String category,
                            List<String> imagePath, List<String> tags) {
        dataSource.saveProduct(productName, description, isNew, canBargain, price, oldPrice,
                category, imagePath, tags);
    }

    public void queryProduct(String id, String type){
        if (type.equals("productInfo")){
            dataSource.queryProductAllInfo(id);
        } else if (type.equals("chat")) {
            dataSource.queryProductForChat(id);
        }
    }

    public void queryProduct(String id, String queryKeys, String activity){
        dataSource.queryProduct(id, queryKeys, activity);
    }

    public void queryProductsForHome(Boolean policy) {
        dataSource.queryProductsForHome(policy);
    }

    public void queryMyPublishProducts() {
        dataSource.queryMyPublishProducts();
    }

    public void queryProductsByCategory(String category) {dataSource.queryProductsByCategory(category); }

}
