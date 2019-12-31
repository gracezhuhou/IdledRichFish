package com.sufe.idledrichfish;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sufe.idledrichfish.data.CreditDataSource;
import com.sufe.idledrichfish.data.CreditRepository;
import com.sufe.idledrichfish.data.ProductDataSource;
import com.sufe.idledrichfish.data.ProductRepository;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class CreditActivity extends AppCompatActivity {

    private MaterialRatingBar rating_bar;

    private String orderId;
    private String sellerId;
    private String productId;
    private CreditRepository creditRepository = CreditRepository.getInstance(new CreditDataSource());
    private ProductRepository productRepository = ProductRepository.getInstance(new ProductDataSource());
    static public Handler creditHandler;
    static public Handler productHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        initData();
        setToolbar();
        setHandler();

        rating_bar = findViewById(R.id.rating_bar);

        final Button button_submit = findViewById(R.id.button_submit);
        button_submit.setOnClickListener(view -> {
            // 提交评价
            creditRepository.saveOrder(rating_bar.getNumStars(), sellerId, orderId);
        });
    }

    @SuppressLint("HandlerLeak")
    private void setHandler() {
        // 获取商品数据 初始化界面
        productHandler = new Handler() {
            public void handleMessage(Message msg) {
                Bundle b = msg.getData();
                if (b.getInt("errorCode") == 0) {
                    final ImageView image_product = findViewById(R.id.image_product);
                    final TextView text_name = findViewById(R.id.text_name);
                    text_name.setText(b.getString("name"));
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.drawable.ic_no_image) // 图片加载出来前，显示的图片
                            .fallback(R.drawable.ic_no_image) // url为空的时候,显示的图片
                            .error(R.drawable.ic_fail); // 图片加载失败后，显示的图片
                    Glide.with(getApplicationContext()).load(b.getByteArray("image1")).apply(options).into(image_product);
                    Log.i("Handler", "Query Product");
                } else {
                    // 9016 网络问题
                    Toast.makeText(getApplicationContext(), b.getString("e"), Toast.LENGTH_LONG).show();
                }
            }
        };
        // 评价保存成功与否
        creditHandler = new Handler() {
            public void handleMessage(Message msg) {
                Bundle b = msg.getData();
                if (b.getInt("errorCode") == 0) {
                    orderId = b.getString("orderId");
                    Log.i("Handler", "Save Credit");
                    Toast.makeText(getApplicationContext(), "评价成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // 9016 网络问题
                    Toast.makeText(getApplicationContext(), b.getString("e"), Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private void initData(){
        Intent intent = getIntent();
        productId = intent.getStringExtra("product_id_extra");
        orderId = intent.getStringExtra("order_id_extra");
        sellerId = intent.getStringExtra("seller_id_extra");
        productRepository.queryProduct(productId, "objectId,name,image1", "credit");
    }

    private void setToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);   // 有返回箭头
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        }
        // 返回键监听
        toolbar.setNavigationOnClickListener(view -> finish());
    }
}
