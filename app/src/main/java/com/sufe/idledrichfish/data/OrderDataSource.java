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

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

public class OrderDataSource {

    /**
     * 上传订单数据
     */
    void saveOrder(String sellerId, String productId) {
        Message msg = new Message();
        Bundle b = new Bundle();

        Order order = new Order();
        order.setBuyer(Student.getCurrentUser(Student.class));
        Student seller = new Student();
        seller.setObjectId(sellerId);
        order.setSeller(seller);
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
}
