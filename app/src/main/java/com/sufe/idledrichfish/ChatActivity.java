package com.sufe.idledrichfish;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sufe.idledrichfish.data.OrderDataSource;
import com.sufe.idledrichfish.data.OrderRepository;
import com.sufe.idledrichfish.data.ProductDataSource;
import com.sufe.idledrichfish.data.ProductRepository;

public class ChatActivity extends AppCompatActivity {

    private String productId;
    private String sellerId;
    private String sellerName;
    private String orderId;
    static public Handler productHandler;
    static public Handler orderHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initData();
        setHandler();

        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(sellerName);

        final Button button_buy = findViewById(R.id.button_buy);
        button_buy.setOnClickListener(view -> buy());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent.getBooleanExtra("order_extra", false)) {
            final LinearLayout layout_product = findViewById(R.id.layout_product);
            layout_product.setVisibility(View.GONE);
        }
    }

    /**
     * 获取intent传入的数
     */
    private void initData() {
        Intent intent = getIntent();
        productId = intent.getStringExtra("product_id_extra");
        sellerId = intent.getStringExtra("seller_id_extra");
        sellerName = intent.getStringExtra("seller_Name_extra");
        ProductRepository.getInstance(new ProductDataSource()).queryProduct(productId, "chat");
    }

    /**
     * 获取商品数据 & 获取订单创建数据
     */
    @SuppressLint("HandlerLeak")
    private void setHandler() {
        final TextView text_product_name = findViewById(R.id.text_product_name);
        final TextView text_price = findViewById(R.id.text_price);
        final ImageView image_product = findViewById(R.id.image_product);
        productHandler = new Handler() {
            public void handleMessage(Message msg) {
                Bundle b = msg.getData();
                if (b.getInt("errorCode") == 0) {
                    // 初始化界面
                    text_product_name.setText(b.getString("name"));
                    text_price.setText(String.valueOf(b.getDouble("price")));
                    Glide.with(getApplicationContext()).load(b.getByteArray("image")).into(image_product);
                    Log.i("Handler", "Query Product");
                } else {
                    // 9016 网络问题
                }
            }
        };

        orderHandler = new Handler() {
            public void handleMessage(Message msg) {
                Bundle b = msg.getData();
                if (b.getInt("errorCode") == 0) {
                    orderId = b.getString("orderId");
                    Log.i("Handler", "Save Order");
                    // 跳转至订单详细界面
                    Intent intent = new Intent(getApplicationContext(), OrderInfoActivity.class);
                    intent.putExtra("product_id_extra", productId);
                    intent.putExtra("seller_id_extra", sellerId);
                    intent.putExtra("seller_name_extra", sellerName);
                    intent.putExtra("order_id_extra", orderId);
                    startActivity(intent);
                } else {
                    // 9016 网络问题
                }
            }
        };
    }

    /**
     * 点击购买
     * 跳转至订单界面
     */
    private void buy() {
        AlertDialog alertDialog = new AlertDialog.Builder(ChatActivity.this)
                .setTitle("确定创建订单？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        OrderRepository.getInstance(new OrderDataSource()).saveOrder(sellerId, productId);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .create();
        alertDialog.show();
    }

}
