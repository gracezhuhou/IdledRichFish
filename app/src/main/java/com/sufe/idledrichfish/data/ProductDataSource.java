package com.sufe.idledrichfish.data;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.sufe.idledrichfish.MyPublishActivity;
import com.sufe.idledrichfish.ProductInfoActivity;
import com.sufe.idledrichfish.data.model.Product;
import com.sufe.idledrichfish.data.model.Student;
import com.sufe.idledrichfish.ui.home.HomeFragment;
import com.sufe.idledrichfish.ui.publish.PublishFragment;

import java.sql.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class ProductDataSource {

    /**
     * 上传商品数据
     */
    public void saveProduct(String productName, String description, boolean isNew, boolean canBargain,
                            double price, double oldPrice, BmobRelation labels, String category) {
        Product product = new Product();
        product.setName(productName);
        product.setDescription(description);
        product.setNew(isNew);
        product.setCanBargain(canBargain);
        product.setPrice(price);
        product.setOldPrice(oldPrice);
        product.setTabs(labels);
        product.setCategory(category);
        product.setSeller(Student.getCurrentUser(Student.class)); // 获取当前用户
        product.setPublishDate(new BmobDate(Tool.getNetTime())); // 获取网络时间
        //todo: product.setImage();

        product.save(new SaveListener<String>() {
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
    public void updateProduct(Product product) {
        product.update(product.getObjectId(), new UpdateListener() {
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
        Product product = new Product();
        product.delete(objectId, new UpdateListener() {
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

    /**
     * 根据Id查商品
     */
    public void queryProduct(String objectId) {
        BmobQuery<Product> bmobQuery = new BmobQuery<>();
        bmobQuery.include("seller");
//        bmobQuery.include("Student,product.seller");

        bmobQuery.getObject(objectId, new QueryListener<Product>() {
            @Override
            public void done(Product product, BmobException e) {
                Message msg = new Message();
                Bundle b = new Bundle();
                if (e == null) {
                    b.putInt("errorCode", 0);
                    b.putString("name", product.getName());
                    b.putString("description", product.getDescription());
                    b.putDouble("price", product.getPrice());
                    b.putDouble("oldPrice", product.getOldPrice());
                    b.putBoolean("isNew", product.isNew());
                    b.putBoolean("canBargain", product.isCanBargain());
                    b.putString("publishDate", product.getPublishDate().getDate()); // String 存放BmobData
                    b.putString("sellerId", product.getSeller().getObjectId());
                    b.putString("sellerName", product.getSeller().getName());
                    b.putFloat("credit", product.getSeller().getCredit());
                    b.putString("gender", product.getSeller().getGender());
                    b.putString("lastLoginDate", product.getSeller().getLastLoginDate().getDate());
                    if (product.getSeller().getImage() != null) {
                        b.putString("studentImage", product.getSeller().getImage().getFileUrl());
                    } else {
                        b.putString("studentImage", "");
                    }
                    if (product.getImage1() != null) {
                        b.putString("image1", product.getImage1().getFileUrl());
                    } else {
                        b.putString("image1", "");
                    }
                    if (product.getImage2() != null) {
                        b.putString("image2", product.getImage2().getFileUrl());
                    } else {
                        b.putString("image2", "");
                    }
                    if (product.getImage3() != null) {
                        b.putString("image3", product.getImage3().getFileUrl());
                    } else {
                        b.putString("image3", "");
                    }
                    if (product.getImage4() != null) {
                        b.putString("image4", product.getImage4().getFileUrl());
                    } else {
                        b.putString("image4", "");
                    }
                    if (product.getImage5() != null) {
                        b.putString("image5", product.getImage5().getFileUrl());
                    } else {
                        b.putString("image5", "");
                    }
                    if (product.getImage6() != null) {
                        b.putString("image6", product.getImage6().getFileUrl());
                    } else {
                        b.putString("image6", "");
                    }
                    if (product.getImage7() != null) {
                        b.putString("image7", product.getImage7().getFileUrl());
                    } else {
                        b.putString("image7", "");
                    }
                    if (product.getImage8() != null) {
                        b.putString("image8", product.getImage8().getFileUrl());
                    } else {
                        b.putString("image8", "");
                    }if (product.getImage9() != null) {
                        b.putString("image9", product.getImage9().getFileUrl());
                    } else {
                        b.putString("image9", "");
                    }
                    msg.setData(b);
                    ProductInfoActivity.productInfoHandler.sendMessage(msg);
                    Log.i("BMOB", "Query Student Success");
                } else {
                    b.putInt("errorCode", e.getErrorCode());
                    b.putString("e", e.toString());
                    Log.e("BMOB", "Query Student Fail", e);
                }

            }
        });
    }
    /**
     * 查询商品
     */
    public void queryProductForHome() {
        BmobQuery<Product> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<Product>() {
            @Override
            public void done(List<Product> products, BmobException e) {
                Message msg = new Message();
                Bundle bundles = new Bundle();
                if (e == null) {
                    bundles.putInt("errorCode", 0);
                    int i = 0;
                    for (Product product : products) {
                        Bundle bundle = new Bundle();
                        bundle.putString("objectId", product.getObjectId());
                        bundle.putString("name", product.getName());
                        bundle.putDouble("price", product.getPrice());
                        bundle.putBoolean("isNew", product.isNew());
                        bundle.putBoolean("canBargain", product.isCanBargain());
                        bundle.putString("sellerId", product.getSeller().getObjectId());
                        if (product.getImage1() != null) {
                            bundle.putString("productImage", product.getImage1().getFileUrl());
                        } else{
                            bundle.putString("productImage", "");
                        }
                        bundles.putBundle(String.valueOf(i), bundle);
                        ++i;
                    }

                        msg.setData(bundles);
                    HomeFragment.homeProductsHandler.sendMessage(msg);

                    Log.i("BMOB", "Query Products Success");
                } else {
                    bundles.putInt("errorCode", e.getErrorCode());
                    msg.setData(bundles);
                    HomeFragment.homeProductsHandler.sendMessage(msg);
                    Log.e("BMOB", "Query Products Fail", e);
                }
            }
        });
    }
}
