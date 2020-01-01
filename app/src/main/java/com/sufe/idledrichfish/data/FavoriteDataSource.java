package com.sufe.idledrichfish.data;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.sufe.idledrichfish.ui.productinfo.ProductInfoActivity;
import com.sufe.idledrichfish.data.model.Favorite;
import com.sufe.idledrichfish.data.model.Product;
import com.sufe.idledrichfish.data.model.Student;
import com.sufe.idledrichfish.ui.myFavorite.MyFavoriteActivity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class FavoriteDataSource {
    /**
     * 添加当前学生的收藏商品
     */
    void saveFavorite(String productId) {
        final Favorite favorite = new Favorite();
        Student student = Student.getCurrentUser(Student.class);
        Product product = new Product();
        product.setObjectId(productId);
        favorite.setStudent(student);
        favorite.setProduct(product);
        favorite.save(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException e) {
                Message msg = new Message();
                Bundle b = new Bundle();
                if(e == null){
                    b.putInt("errorCode", 0);
                    Log.i("BMOB","Save Favorite Success");
                }else{
                    b.putInt("errorCode", e.getErrorCode());
                    b.putString("e", e.toString());
                    Log.e("BMOB","Save Favorite Fail", e);
                }
                msg.setData(b);
                ProductInfoActivity.addFavoriteHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 取消当前学生对此商品的收藏
     */
    void removeFavorite(String productId) {
        // 先查出在Favorite表中的Id
        BmobQuery<Favorite> query = new BmobQuery<Favorite>();
        Student student = Student.getCurrentUser(Student.class);
        Product product = new Product();
        product.setObjectId(productId);
        query.addWhereEqualTo("student",new BmobPointer(student));
        query.addWhereEqualTo("product",new BmobPointer(product));
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY); // 网络
        query.findObjects(new FindListener<Favorite>() {
            @Override
            public void done(List<Favorite> objects, BmobException e) {
                Message msg = new Message();
                Bundle b = new Bundle();
                if (e == null) {
                    Log.i("BMOB", "Query FavoriteId Success");
                    for (Favorite favorite: objects) {
                        // 删除favorite
                        favorite.delete(favorite.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    b.putInt("errorCode", 0);
                                    Log.i("BMOB", "Delete Favorite Success");
                                } else {
                                    b.putInt("errorCode", e.getErrorCode());
                                    b.putString("e", e.toString());
                                    Log.e("BMOB", "Delete Favorite Fail", e);
                                }
                                msg.setData(b);
                                ProductInfoActivity.cancelFavoriteHandler.sendMessage(msg);
                            }
                        });
                    }
                } else {
                    Log.e("BMOB", "Query FavoriteId Fail", e);
                    b.putInt("errorCode", e.getErrorCode());
                    b.putString("e", e.toString());
                    msg.setData(b);
                    ProductInfoActivity.cancelFavoriteHandler.sendMessage(msg);
                }
            }
        });
    }

    /**
     * 当前学生是否收藏了此商品
     */
    void isFavorite(String productId) {
        BmobQuery<Favorite> query = new BmobQuery<Favorite>();
        Student student = Student.getCurrentUser(Student.class);
        Product product = new Product();
        product.setObjectId(productId);
        query.addWhereEqualTo("student",new BmobPointer(student));
        query.addWhereEqualTo("product",new BmobPointer(product));
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY); // 网络
        query.findObjects(new FindListener<Favorite>() {
            @Override
            public void done(List<Favorite> objects, BmobException e) {
                Message msg = new Message();
                Bundle b = new Bundle();
                if (e == null) {
                    Log.i("BMOB", "Query Favorite Success");
                    b.putInt("errorCode", 0);
                    if (objects.isEmpty()) {
                        b.putBoolean("favorite", false);
                    } else {
                        b.putBoolean("favorite", true);
                    }
                } else {
                    Log.e("BMOB", "Query Favorite Fail", e);
                    b.putInt("errorCode", e.getErrorCode());
                    b.putString("e", e.toString());
                }
                msg.setData(b);
                ProductInfoActivity.isFavoriteHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 获取当前用户的所有收藏
     */
    void queryMyFavorite() {
        BmobQuery<Favorite> query = new BmobQuery<Favorite>();
        Student student = Student.getCurrentUser(Student.class);
        query.addWhereEqualTo("student",new BmobPointer(student));
        query.include("product.seller");
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY); // 网络
        query.findObjects(new FindListener<Favorite>() {
            @Override
            public void done(List<Favorite> objects, BmobException e) {
                Message msg = new Message();
                Bundle bundles = new Bundle();
                if (e == null) {
                    Log.i("BMOB", "Query My Favorite Success");
                    bundles.putInt("errorCode", 0);
                    int i = 0;
                    for (Favorite favorite: objects) {
                        Bundle b = new Bundle();
                        Product product = favorite.getProduct();
                        Student seller = product.getSeller();
                        b.putString("productId", product.getObjectId());
                        b.putString("name", product.getName());
                        b.putDouble("price", product.getPrice());
                        b.putString("sellerId", seller.getObjectId());
                        b.putString("sellerName", seller.getName());
                        b.putFloat("credit", seller.getCredit());
                        // todo P image 1-4 & S image
                        bundles.putBundle(String.valueOf(i), b);
                        ++i;
                    }
                } else {
                    Log.e("BMOB", "Query My Favorite Fail", e);
                    bundles.putInt("errorCode", e.getErrorCode());
                    bundles.putString("e", e.toString());
                }
                msg.setData(bundles);
                MyFavoriteActivity.myFavoriteHandler.sendMessage(msg);
            }
        });
    }
}
