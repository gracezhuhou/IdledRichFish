package com.sufe.idledrichfish.data;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.google.common.primitives.Bytes;
import com.sufe.idledrichfish.OrderInfoActivity;
import com.sufe.idledrichfish.data.model.Order;
import com.sufe.idledrichfish.data.model.Product;
import com.sufe.idledrichfish.data.model.Student;
import com.sufe.idledrichfish.ui.chat.ChatActivity;
import com.sufe.idledrichfish.ui.myOrder.PlaceholderFragment;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class OrderDataSource {

    /**
     * 上传订单数据
     */
    void saveOrder(String productId) {
        Message msg = new Message();
        Bundle b = new Bundle();

        Order order = new Order();
        order.setBuyer(Student.getCurrentUser(Student.class));
        Product product = new Product();
        product.setObjectId(productId);
        order.setProduct(product);
        order.setStatus(0);
        order.setOrderDate(new BmobDate(Tool.getNetTime())); // 获取网络时间

        order.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                // 反馈给ChatActivity
                if(e == null) {
                    b.putInt("errorCode", 0);
                    b.putString("orderId", objectId);
                    msg.setData(b);
                    ChatActivity.orderHandler.sendMessage(msg);
                    Log.i("BMOB", "Save Order Success");
                }
                else {
                    b.putInt("errorCode", e.getErrorCode());
                    b.putString("e", e.toString());
                    msg.setData(b);
                    ChatActivity.orderHandler.sendMessage(msg);
                    Log.e("BMOB", "Save Order Fail", e);
                }
            }
        });
    }

    /**
     * 根据Id查询订单
     */
    void queryOrder(String objectId) {
        BmobQuery<Order> bmobQuery = new BmobQuery<>();
        bmobQuery.include("product");
        bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK); // 优先缓存
        bmobQuery.getObject(objectId, new QueryListener<Order>() {
            @Override
            public void done(Order order, BmobException e) {
                Message msg = new Message();
                Bundle b = new Bundle();
                if (e == null) {
                    b.putInt("errorCode", 0);
                    Product product = order.getProduct();
                    b.putString("productName", product.getName());
                    b.putDouble("price", product.getPrice());
                    if (product.getImage1() != null) {
                        b.putByteArray("productImage", Bytes.toArray(product.getImage1()));
                    }
                    b.putString("date", order.getOrderDate().getDate());
                    b.putInt("status", order.getStatus());

                    Log.i("BMOB", "Query Order By Id Success");
                } else {
                    b.putInt("errorCode", e.getErrorCode());
                    b.putString("e", e.toString());
                    Log.e("BMOB", "Query Order By Id Fail", e);
                }
                msg.setData(b);
                OrderInfoActivity.orderHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 更新订单状态
     * @param status 进行中-0 已完成-1 已关闭-2
     */
    void updateOrderStatus(String objectId, int status) {
        Order order = new Order();
        order.setStatus(status);
        if (status == 1|| status == 2) {
            order.setFinishDate(new BmobDate(Tool.getNetTime())); // 获取网络时间
        }
        order.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                Message msg = new Message();
                Bundle b = new Bundle();
                if(e == null) {
                    b.putInt("errorCode", 0);
                    b.putInt("status", status);
                    msg.setData(b);
                    OrderInfoActivity.statusHandler.sendMessage(msg);
                    Log.i("BMOB", "Update Product Success");
                }
                else {
                    b.putInt("errorCode", e.getErrorCode());
                    b.putString("e", e.toString());
                    msg.setData(b);
                    OrderInfoActivity.statusHandler.sendMessage(msg);
                    Log.e("BMOB", "Update Product Fail", e);
                }
            }
        });
    }

    /**
     * 获取当前用户全部/已完成/进行中/已关闭订单
     */
    void queryOrders(int status) {
        BmobQuery<Order> query = new BmobQuery<Order>();
        Student student = Student.getCurrentUser(Student.class);
        query.addWhereEqualTo("buyer", new BmobPointer(student));
        if (status != -1) {
            query.addWhereEqualTo("status", status);
        }
        query.include("product.seller");
        query.findObjects(new FindListener<Order>() {
            @Override
            public void done(List<Order> objects, BmobException e) {
                Message msg = new Message();
                Bundle bundles = new Bundle();
                if (e == null) {
                    Log.i("BMOB", "Query My Order Success" + objects.size());
                    bundles.putInt("errorCode", 0);
                    int i = 0;
                    for (Order order: objects) {
                        Bundle b = new Bundle();
                        Product product = order.getProduct();
                        Student seller = product.getSeller();
                        b.putString("productId", product.getObjectId());
                        b.putString("name", product.getName());
                        b.putDouble("price", product.getPrice());
                        b.putString("sellerId", seller.getObjectId());
                        b.putString("sellerName", seller.getName());
                        if (product.getImage1() != null) {
                            b.putByteArray("productImage", Bytes.toArray(product.getImage1()));
                        }
                        if (seller.getImage() != null) {
                            b.putByteArray("sellerImage", Bytes.toArray(product.getImage1()));
                        }
                        bundles.putBundle(String.valueOf(i), b);
                        ++i;
                    }
                } else {
                    Log.e("BMOB", "Query My Favorite Fail", e);
                    bundles.putInt("errorCode", e.getErrorCode());
                    bundles.putString("e", e.toString());
                }
                msg.setData(bundles);
                PlaceholderFragment.orderHandler.sendMessage(msg);
            }
        });
    }
}
