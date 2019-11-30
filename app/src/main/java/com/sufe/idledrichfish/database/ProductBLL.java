package com.sufe.idledrichfish.database;

import java.util.List;

public class ProductBLL {
    private ProductDAL productDAL;

    public ProductBLL() {
        productDAL = new ProductDAL();
    }

    public boolean insertProduct(Product product) {
        String product_id = product.getProductId() + System.currentTimeMillis();
        product.setProductId(product_id);
        return productDAL.insertProduct(product);
    }

    public boolean deleteProductById(String id) {
        return productDAL.deleteProductById(id);
    }

    public Product getProductById(String id) {
        return productDAL.getProductById(id);
    }

//    暂时
    public List<Product> getAllProducts() {
        return productDAL.getAllProducts();
    }
}
