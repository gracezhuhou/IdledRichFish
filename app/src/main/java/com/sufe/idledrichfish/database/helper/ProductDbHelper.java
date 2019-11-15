package com.sufe.idledrichfish.database.helper;

import android.util.Log;

import com.sufe.idledrichfish.database.Label;
import com.sufe.idledrichfish.database.Product;

import org.litepal.LitePal;

import java.util.Date;
import java.util.List;

public class ProductDbHelper extends DbHelper {
    public ProductDbHelper() {
        super();
    }

    /*
        添加商品
     */
    public Boolean add(String name, String description, double price, String publisher_id, List<Label> labels, String category) {
        Product product = new Product();
        String product_id = publisher_id + System.currentTimeMillis();
        product.setProductId(product_id);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        //product.setPublishDate(publish_date);
        product.setPublisherId(publisher_id);
        product.setLabels(labels);
        product.setCategory(category);

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
            Log.v("Database","Add Product Success");
            return true;
        }
        else {
            Log.v("Database", "Add Product Fail");
            return false;
        }
    }

    public List<Product> getProducts() {
        List<Product> products = LitePal.findAll(Product.class);
        return products;
    }

    /*
        寻找某一学生发布的所有产品
     */
    public List<Product> findByPublisher(String student_id) {
        return LitePal.where("publisher_id = ?", student_id).find(Product.class);
    }
}
