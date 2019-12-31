package com.sufe.idledrichfish;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.sufe.idledrichfish.data.OrderDataSource;
import com.sufe.idledrichfish.data.OrderRepository;

public class OrderInfoActivity extends AppCompatActivity {

    private TextView text_status;
    private Button button_finish;
    private Button button_cancel;
    private Button button_credit;
    private Button button_admin;

    private String productId;
    private String sellerId;
    private String sellerName;
    private String orderId;
    static public Handler orderHandler;
    static public Handler statusHandler;
    private OrderRepository orderRepository = OrderRepository.getInstance(new OrderDataSource());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);

        text_status = findViewById(R.id.text_status);
        button_finish = findViewById(R.id.button_finish);
        button_cancel = findViewById(R.id.button_cancel);
        button_credit = findViewById(R.id.button_credit);
        button_admin = findViewById(R.id.button_admin);

        initData();
        setHandler();
        setToolbar();

        // 完成订单
        button_finish.setOnClickListener(view -> {
            orderRepository.updateOrderStatus(orderId, 1);
        });
        // 关闭订单
        button_cancel.setOnClickListener(view -> {
            orderRepository.updateOrderStatus(orderId, 2);
        });
    }

    /**
     * 获取intent传入的da数据
     */
    private void initData() {
        Intent intent = getIntent();
        productId = intent.getStringExtra("product_id_extra");
        sellerId = intent.getStringExtra("seller_id_extra");
        sellerName = intent.getStringExtra("seller_Name_extra");
        orderId = intent.getStringExtra("order_id_extra");
        final TextView text_seller_name = findViewById(R.id.text_seller_name);
        text_seller_name.setText(sellerName);
        OrderRepository.getInstance(new OrderDataSource()).queryOrder(orderId);
    }

    /**
     * 获取订单数据 & 更新订单状态
     */
    @SuppressLint("HandlerLeak")
    private void setHandler() {
        // 获取订单数据
        orderHandler = new Handler() {
            public void handleMessage(Message msg) {
                Bundle b = msg.getData();
                if (b.getInt("errorCode") == 0) {
                    // 初始化界面
                    final TextView text_order_id = findViewById(R.id.text_order_id);
                    String orderIdText = "订单号：" + orderId;
                    text_order_id.setText(orderIdText);

                    int status = b.getInt("status");
                    switch (status) {
                        case 0: text_status.setText("订单进行中");
                            break;
                        case 1: text_status.setText("订单已完成");
                            break;
                        case 2: text_status.setText("订单已关闭");
                    }
                    final ImageView image_product = findViewById(R.id.image_product);
                    Glide.with(getApplicationContext()).load(b.getByteArray("productImage")).into(image_product);
                    final TextView text_product_name = findViewById(R.id.text_product_name);
                    text_product_name.setText(b.getString("productName"));
                    final TextView text_price = findViewById(R.id.text_price);
                    text_price.setText(String.valueOf(b.getDouble("price")));
                    final TextView text_order_date = findViewById(R.id.text_order_date);
                    text_order_date.setText(b.getString("date"));
                    Log.i("Handler", "Query Order");
                } else {
                    // 9016 网络问题
                }
            }
        };
        // 更新订单状态是否成功
        statusHandler = new Handler() {
            public void handleMessage(Message msg) {
                Bundle b = msg.getData();
                if (b.getInt("errorCode") == 0) {
                    int status = b.getInt("status");
                    if (status == 1) {
                        text_status.setText("订单已完成");
                        button_finish.setVisibility(View.GONE);
                        button_cancel.setVisibility(View.GONE);
                        button_credit.setVisibility(View.VISIBLE);
                        button_admin.setVisibility(View.VISIBLE);
                    } else if (status == 2) {
                        text_status.setText("订单已关闭");
                        button_finish.setVisibility(View.GONE);
                        button_cancel.setVisibility(View.GONE);
                        button_admin.setVisibility(View.VISIBLE);
                    }
                    Log.i("Handler", "Query Order");
                } else {
                    // 9016 网络问题
                }
            }
        };
    }

    private void setToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);   // 有返回箭头
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        }
        // 返回键监听
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), OrderInfoActivity.class);
            intent.putExtra("order_extra", true);
            finish();
        });
    }
}
