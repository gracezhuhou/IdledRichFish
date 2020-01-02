package com.sufe.idledrichfish;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sufe.idledrichfish.data.OrderDataSource;
import com.sufe.idledrichfish.data.OrderRepository;

public class OrderInfoActivity extends AppCompatActivity {

    private String productId;
    private String sellerId;
    private String sellerName;
    private String orderId;
    static public Handler orderHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);

        initData();
        setHandler();

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);   // 有返回箭头
        }
        // 返回键监听
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OrderInfoActivity.class);
                intent.putExtra("order_extra", true);
                finish();
            }
        });
    }

    /**
     * 获取intent传入的数
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
     * 获取商品数据&初始化界面
     */
    @SuppressLint("HandlerLeak")
    private void setHandler() {
        orderHandler = new Handler() {
            public void handleMessage(Message msg) {
                Bundle b = msg.getData();
                if (b.getInt("errorCode") == 0) {
                    // 初始化界面
                    final TextView text_order_id = findViewById(R.id.text_order_id);
                    String orderIdText = "订单号：" + orderId;
                    text_order_id.setText(orderIdText);
                    final TextView text_status = findViewById(R.id.text_status);
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
    }
}
