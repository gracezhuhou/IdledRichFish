package com.sufe.idledrichfish;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sufe.idledrichfish.data.ProductDataSource;
import com.sufe.idledrichfish.data.ProductRepository;
import com.sufe.idledrichfish.data.model.Student;

import java.text.DecimalFormat;
import java.util.Objects;

import cn.bmob.v3.datatype.BmobDate;

public class ProductInfoActivity extends AppCompatActivity {

    private TextView text_product_name;
    private TextView text_price;
    private TextView text_old_price;
    private TextView text_product_description;
    private TextView text_seller_name;
    private TextView text_seller_credit;
    private TextView text_login_date;
    private TextView text_publish_date;
    private ImageView image_product;
    private ImageView image_seller;
    private ImageView icon_gender;
    private CardView card_new;
    private CardView card_cannot_bargain;
    private ConstraintLayout layout_seller;

    private String productId;
    private String sellerId;
    private String sellerName;
    static public Handler productInfoHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        text_product_name = findViewById(R.id.text_product_name);
        text_price = findViewById(R.id.text_price);
        text_old_price = findViewById(R.id.text_old_price);
        text_product_description = findViewById(R.id.text_product_description);
        text_seller_name = findViewById(R.id.text_seller_name);
        text_seller_credit = findViewById(R.id.text_seller_credit);
        text_login_date = findViewById(R.id.text_login_date);
        text_publish_date = findViewById(R.id.text_publish_date);
        image_product = findViewById(R.id.image_product);
        image_seller = findViewById(R.id.image_seller);
        icon_gender = findViewById(R.id.icon_gender);
        card_new = findViewById(R.id.card_new);
        card_cannot_bargain = findViewById(R.id.card_cannot_bargain);
        layout_seller = findViewById(R.id.layout_seller);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   // 有返回箭头

        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                Log.d("STATE", state.name());
                if( state == State.EXPANDED ) {
                    //展开状态
                    layout_seller.setVisibility(View.VISIBLE);
                    Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
                    getSupportActionBar().setIcon(null);
                }else if(state == State.COLLAPSED){
                    //折叠状态
                    layout_seller.setVisibility(View.INVISIBLE);
                    Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
                    if (sellerName != null) {
                        getSupportActionBar().setTitle(sellerName);
                    }
                    // todo:设置标题头像
                    getSupportActionBar().setIcon(R.drawable.ic_user); // 暂时

                }else {
                    //中间状态
                    layout_seller.setVisibility(View.VISIBLE);
                    Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
                    getSupportActionBar().setIcon(null);
                }
            }
        });
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                float alpha = ((float)98 + (float)i) / (float)98;
                layout_seller.setAlpha(alpha);
                Log.i("AppBar", String.valueOf(i));
            }
        });


        initData();

        setHandler();


        /*
         * 返回键监听
         */
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*
         * fab按钮监听
         */
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    /**
     * 根据传入的product_id
     * 初始化界面
     */
    private void initData() {
        Intent intent = getIntent();
        productId = intent.getStringExtra("product_id_extra");
        ProductRepository.getInstance(new ProductDataSource()).queryProduct(productId);
    }

    /**
     * 根据Bmob传回的数据
     * 设定界面
     */
    @SuppressLint("HandlerLeak")
    private void setHandler() {
        // 获取商品信息
        productInfoHandler = new Handler() {
            public void handleMessage(Message msg) {
                Bundle b = msg.getData();
                if (b.getInt("errorCode") == 0) {
                    // 商品信息
                    text_product_name.setText(b.getString("name"));
                    text_product_description.setText(b.getString("description"));
                    DecimalFormat format1 = new java.text.DecimalFormat("¥ 0.00"); // 保留小数点两位
                    text_price.setText(format1.format(b.getDouble("price")));
                    text_old_price.setText(format1.format(b.getDouble("oldPrice")));
                    if(!b.getBoolean("isNew")) {
                        card_new.setVisibility(View.GONE);
                    }
                    if (b.getBoolean("canBargain")) {
                        card_cannot_bargain.setVisibility(View.GONE);
                    }
                    String publishDate = "发布于" + b.getString("publishDate");
                    text_publish_date.setText(publishDate);
                    // 卖家信息
                    sellerId = b.getString("sellerId");
                    sellerName = b.getString("sellerName");
                    text_seller_name.setText(sellerName);
                    DecimalFormat format2 = new java.text.DecimalFormat("0.0");
                    text_seller_credit.setText(format2.format(b.getFloat("credit")));
                    if (Objects.requireNonNull(b.getString("gender")).equals("female")) {
                        icon_gender.setImageResource(R.drawable.ic_woman);
                    }
                    String lastLoginDate = "最晚" + b.getString("lastLoginDate") + "来过";
                    text_login_date.setText(lastLoginDate);
                }
                Log.i("Handler", "Query Products Info");
            }
        };
    }

}
