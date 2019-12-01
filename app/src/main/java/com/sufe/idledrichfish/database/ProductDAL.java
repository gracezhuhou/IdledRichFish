package com.sufe.idledrichfish.database;

import android.util.Log;

import org.litepal.LitePal;

import java.util.List;

public class ProductDAL {
    private DBHelper dbHelper;

    public ProductDAL() {
        dbHelper = new DBHelper();
    }

    /*
    添加商品
     */
    public Boolean insertProduct(Product product) {
//        Product product = new Product();
//        String product_id = publisher_id + System.currentTimeMillis();
//        product.setProductId(product_id);
//        product.setName(name);
//        product.setDescription(description);
//        product.setPrice(price);
//        //product.setPublishDate(publish_date);
//        product.setPublisherId(publisher_id);
//        product.setLabels(labels);
//        product.setCategory(category);

        // todo: id & image
        //获取图片
//        String picPath = Environment.getExternalStorageDirectory().getPath() + "/cardPic.jpg";
//        Bitmap pic= BitmapFactory.decodeFile(picPath);
//        if (pic != null) {
//            //把图片转换字节流
//            byte[] images = img();
//            card.setPicture(images);    //保存图片
//        }

        if (product.save()) {
            Log.i("Database","Add Product Success");
            return true;
        }
        else {
            Log.i("Database", "Add Product Fail");
            return false;
        }
    }

    /*
    根据id删除商品
     */
    public boolean deleteProductById(String id) {
        int deleteNum = LitePal.deleteAll(BmobStudent.class, "productId = ?", id);
        if (deleteNum >= 1) {
            Log.i("Database", "Delete BmobStudent Success :" + deleteNum);
            return true;
        }
        else {
            Log.i("Database", "Delete BmobStudent Fail");
            return false;
        }
    }

    /*
    寻找某一学生发布的所有商品
     */
    public List<Product> findProductsByPublisher(String student_id) {
        return LitePal.where("publisherId = ?", student_id).find(Product.class);
    }

    /*
    根据id寻找商品
     */
    public Product getProductById(String id) {
        List<Product> products = LitePal.where("publisherId = ?", id).find(Product.class);

        if (products.size() == 1)
            return products.get(0);
        else
            return null;
    }

    public List<Product> getAllProducts() {
//        List<Product> products = LitePal.findAll(Product.class);
        return LitePal.findAll(Product.class);
    }
}
