package com.sufe.idledrichfish.database;

import com.sufe.idledrichfish.data.model.BmobProduct;

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

    /*
     * 将Bmob上的Product数据保存至本地数据库
     */
//    public boolean storeProduct(BmobProduct bmobProduct) {
//        Product product = new Product();
//        product.setProductId(bmobProduct.getObjectId());
//        product.setName(bmobProduct.getName());
//        product.setDescription(bmobProduct.getDescription());
//        product.setPrice(bmobProduct.getPrice());
//        product.setOldPrice(bmobProduct.getOldPrice());
//        product.setNew(bmobProduct.isNew());
//        product.setCanBargain(bmobProduct.isCanBargain());
//        product.setPublishDate(bmobProduct.getPublishDate());
//        product.setPublisherId(bmobProduct.getSeller());
//        product.setCategory(bmobProduct.getCategory());
//        product.setTabs(bmobProduct.getBmobRelation());
//        product.setImage1(bmobProduct.getImage1());
//        product.setImage2(bmobProduct.getImage2());
//        product.setImage3(bmobProduct.getImage3());
//        product.setImage4(bmobProduct.getImage4());
//        product.setImage5(bmobProduct.getImage5());
//        product.setImage6(bmobProduct.getImage6());
//        product.setImage7(bmobProduct.getImage7());
//        product.setImage8(bmobProduct.getImage8());
//        product.setImage9(bmobProduct.getImage9());

//        if (productDAL.isStudentExistById(student.getStudentId())) {
//            return studentDAL.updateStudent(student);
//        }
//        else {
//            return studentDAL.insertStudent(student);
//        }
//        return true;
//    }

//    暂时
    public List<Product> getAllProducts() {
        return productDAL.getAllProducts();
    }
}
