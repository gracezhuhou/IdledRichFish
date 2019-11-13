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

    public Boolean addProduct(String name, String description, double price, Date publish_date, String publisher_id, List<Label> labels) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setPublishDate(publish_date);
        product.setPublisherId(publisher_id);
        product.setLabels(labels);


        // todo: id & image
        //获取图片
//        String picPath = Environment.getExternalStorageDirectory().getPath() + "/cardPic.jpg";
//        Bitmap pic= BitmapFactory.decodeFile(picPath);
//        if (pic != null) {
//            //把图片转换字节流
//            byte[] images = img();
//            card.setPicture(images);    //保存图片
//        }
        product.save();

        Log.v("Database","Add Product Success");
        return true;
    }


    public List<Product> getProducts() {
        List<Product> products = LitePal.findAll(Product.class);
        return products;
    }
}
