package com.sufe.idledrichfish.data;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.sufe.idledrichfish.MyPublishActivity;
import com.sufe.idledrichfish.data.model.BmobProduct;
import com.sufe.idledrichfish.data.model.BmobStudent;
import com.sufe.idledrichfish.ui.home.HomeFragment;
import com.sufe.idledrichfish.ui.publish.PublishFragment;

import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class ProductDataSource {

    /*
     * 上传商品数据
     */
    public void saveProduct(String productName, String description, boolean isNew, boolean canBargain,
                            double price, double oldPrice, BmobRelation labels, String category) {
        BmobProduct bmobProduct = new BmobProduct();
        bmobProduct.setName(productName);
        bmobProduct.setDescription(description);
        bmobProduct.setNew(isNew);
        bmobProduct.setCanBargain(canBargain);
        bmobProduct.setPrice(price);
        bmobProduct.setOldPrice(oldPrice);
        bmobProduct.setTabs(labels);
        bmobProduct.setCategory(category);
        bmobProduct.setSeller(BmobStudent.getCurrentUser(BmobStudent.class)); // 获取当前用户
        bmobProduct.setPublishDate(new BmobDate(getNetTime())); // 获取网络时间
        //todo: bmobProduct.setImage();

        bmobProduct.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                // 反馈给PublishmentFragment
                Message msg = new Message();
                Bundle b = new Bundle();
                if(e == null) {
                    b.putInt("errorCode", 0);
                    msg.setData(b);
                    PublishFragment.publishmentHandler.sendMessage(msg);
                    Log.i("BMOB", "Save Product Success");
                }
                else {
                    b.putInt("errorCode", e.getErrorCode());
                    b.putString("e", e.toString());
                    msg.setData(b);
                    PublishFragment.publishmentHandler.sendMessage(msg);
                    Log.e("BMOB", "Save Product Fail", e);
                }
            }
        });
    }

    /**
     * 更新商品数据
     */
    public void updateProduct(BmobProduct bmobProduct) {
        bmobProduct.update(bmobProduct.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                // 反馈给 todo:某activity
                Message msg = new Message();
                Bundle b = new Bundle();
                if(e == null) {
                    b.putInt("errorCode", 0);
                    msg.setData(b);
                    //PublishFragment.publishmentHandler.sendMessage(msg);
                    Log.i("BMOB", "Update Product Success");
                }
                else {
                    b.putInt("errorCode", e.getErrorCode());
                    b.putString("e", e.toString());
                    msg.setData(b);
                    //PublishFragment.publishmentHandler.sendMessage(msg);
                    Log.e("BMOB", "Update Product Fail", e);
                }
            }
        });
    }

    /**
     * 删除一个对象(根据ObjectId)
     */
    public void deleteProduct(String objectId) {
        BmobProduct bmobProduct = new BmobProduct();
        bmobProduct.delete(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                // 反馈给MyPublishedActivity
                Message msg = new Message();
                Bundle b = new Bundle();
                if(e == null) {
                    b.putInt("errorCode", 0);
                    msg.setData(b);
                    MyPublishActivity.myPublishedHandler.sendMessage(msg);
                    Log.i("BMOB", "Delete Product Success");
                }
                else {
                    b.putInt("errorCode", e.getErrorCode());
                    b.putString("e", e.toString());
                    msg.setData(b);
                    MyPublishActivity.myPublishedHandler.sendMessage(msg);
                    Log.e("BMOB", "Delete Product Fail", e);
                }
            }
        });
    }

    /*
     * 查询商品
     */
    /**
     * 查询多条数据
     */
    public void queryProductForHome() {
        BmobQuery<BmobProduct> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<BmobProduct>() {
            @Override
            public void done(List<BmobProduct> products, BmobException e) {
                Message msg = new Message();
                Bundle bundles = new Bundle();
                if (e == null) {
                    bundles.putInt("errorCode", 0);
                    int i = 0;
                    for (BmobProduct product : products) {
                        Bundle bundle = new Bundle();
                        bundle.putString("objectId", product.getObjectId());
                        bundle.putString("name", product.getName());
                        bundle.putDouble("price", product.getPrice());
                        bundle.putBoolean("isNew", product.isNew());
                        bundle.putBoolean("canBargain", product.isCanBargain());
                        if (product.getSeller().getName() != null) {
                            bundle.putString("sellerImage", product.getSeller().getName());
                        } else{
                            bundle.putString("sellerImage", "");
                        }
                        bundle.putFloat("sellerCredit", product.getSeller().getCredit());
                        if (product.getSeller().getImage() != null) {
                            bundle.putString("sellerImage", product.getSeller().getImage().getFileUrl());
                        } else{
                            bundle.putString("sellerImage", "");
                        }
                        if (product.getImage1() != null) {
                            bundle.putString("productImage", product.getImage1().getFileUrl());
                        } else{
                            bundle.putString("sellerImage", "");
                        }
                        bundles.putBundle(String.valueOf(i), bundle);
                        ++i;
                    }

                        msg.setData(bundles);
                    HomeFragment.homeHandler.sendMessage(msg);

                    Log.i("BMOB", "Query Products Success");
                } else {
                    bundles.putInt("errorCode", e.getErrorCode());
                    msg.setData(bundles);
                    HomeFragment.homeHandler.sendMessage(msg);
                    Log.e("BMOB", "Query Products Fail", e);
                }
            }
        });
    }

    /*
     * 获取网络标准时间
     */
    public static Date getNetTime(){
        String webUrl = "http://www.ntsc.ac.cn"; // 中国科学院国家授时中心
        try {
            URL url = new URL(webUrl);
            URLConnection uc = url.openConnection();
            uc.setReadTimeout(5000);
            uc.setConnectTimeout(5000);
            uc.connect();
            long correctTime = uc.getDate();
            return new Date(correctTime);
        } catch (Exception e) {
            return new Date();
        }
    }
}
