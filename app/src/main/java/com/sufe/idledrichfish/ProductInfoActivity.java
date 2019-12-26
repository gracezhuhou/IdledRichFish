package com.sufe.idledrichfish;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.sufe.idledrichfish.data.FavoriteDataSource;
import com.sufe.idledrichfish.data.FavoriteRepository;
import com.sufe.idledrichfish.data.ProductDataSource;
import com.sufe.idledrichfish.data.ProductRepository;

import java.text.DecimalFormat;
import java.util.Objects;

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
    private ImageView icon_favorite;
    private CardView card_new;
    private CardView card_cannot_bargain;
    private ConstraintLayout layout_seller;

    private String productId;
    private String sellerId;
    private String sellerName;
    static public Handler productInfoHandler;
    static public Handler addFavoriteHandler;
    static public Handler cancelFavoriteHandler;
    static public Handler isFavorateHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        initView();

        setAppBar();
        initData();
        setHandler();

        // 点击“收藏”
        final LinearLayout layout_favorate = findViewById(R.id.layout_favorate);
        layout_favorate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (icon_favorite.getTag().equals("favor")) {
                    Log.i("ProductInfo", "Remove Favorate");
                    icon_favorite.setImageResource(R.drawable.ic_favorate_gray);
                    icon_favorite.setTag("unfavor");
                    FavoriteRepository.getInstance(new FavoriteDataSource()).removeFavorite(productId);
                } else {
                    Log.i("ProductInfo", "Add Favorate");
                    icon_favorite.setImageResource(R.drawable.ic_favorate_yellow);
                    icon_favorite.setTag("favor");
                    FavoriteRepository.getInstance(new FavoriteDataSource()).saveFavorite(productId);
                }
            }
        });

        // 点击“联系卖家”
        final Button button_message = findViewById(R.id.button_message);
        button_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("product_id_extra", productId);
                intent.putExtra("seller_id_extra", sellerId);
                intent.putExtra("seller_name_extra", sellerName);
                startActivity(intent);
            }
        });

    }

    /**
     * 设置顶部AppBar
     * 折叠效果
     * Toolbar返回键监听
     */
    private void setAppBar() {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   // 有返回箭头

        final AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        // 折叠状态监听
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                Log.d("STATE", state.name());
                if( state == State.EXPANDED ) {
                    //展开状态
                    layout_seller.setVisibility(View.VISIBLE);
                }else if(state == State.COLLAPSED){
                    //折叠状态
                    layout_seller.setVisibility(View.INVISIBLE);
//                    if (sellerName != null) {
//                        getSupportActionBar().setTitle(sellerName);
//                    }
                    // todo:设置标题头像
                    getSupportActionBar().setIcon(R.drawable.ic_user); // 暂时

                }else {
                    //中间状态
                    layout_seller.setVisibility(View.VISIBLE);
                    getSupportActionBar().setIcon(null);
                }
            }
        });
        // 折叠时淡出效果
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                float alpha = ((float)98 + (float)i) / (float)98;
                layout_seller.setAlpha(alpha);
                Log.i("AppBar", String.valueOf(i));
            }
        });

        // 返回键监听
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 初始化界面: Product, Favorite
     * 根据传入的product_id
     */
    private void initData() {
        Intent intent = getIntent();
        productId = intent.getStringExtra("product_id_extra");
        ProductRepository.getInstance(new ProductDataSource()).queryProduct(productId, "productInfo");
        FavoriteRepository.getInstance(new FavoriteDataSource()).isFavorite(productId);
    }

    /**
     * 根据Bmob传回的数据
     * 设定界面
     * 反馈收藏结果
     */
    @SuppressLint("HandlerLeak")
    private void setHandler() {
        // 获取商品信息 设定界面
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
        // 添加收藏反馈
        addFavoriteHandler = new Handler() {
            public void handleMessage (Message msg){
                Bundle b = msg.getData();
                if (b.getInt("errorCode") != 0) {
                    NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(ProductInfoActivity.this);
                    dialogBuilder
                            .withTitle("添加收藏失败")
                            .withMessage(b.getString("e"))
                            .withDialogColor("#770000")
                            .withIcon(getResources().getDrawable(R.drawable.ic_fail))
                            .withEffect(Effectstype.SlideBottom)
                            .show();
                    icon_favorite.setImageResource(R.drawable.ic_favorate_gray);
                    icon_favorite.setTag("unfavor");
                }
            }
        };
        // 删除收藏反馈
        cancelFavoriteHandler = new Handler() {
            public void handleMessage (Message msg){
                Bundle b = msg.getData();
                if (b.getInt("errorCode") != 0) {
                    NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(ProductInfoActivity.this);
                    dialogBuilder
                            .withTitle("取消收藏失败")
                            .withMessage(b.getString("e"))
                            .withDialogColor("#770000")
                            .withIcon(getResources().getDrawable(R.drawable.ic_fail))
                            .withEffect(Effectstype.SlideBottom)
                            .show();
                    icon_favorite.setImageResource(R.drawable.ic_favorate_yellow);
                    icon_favorite.setTag("favor");
                }
            }
        };
        // 根据是否收藏来设定图标
        isFavorateHandler = new Handler() {
            public void handleMessage (Message msg){
                Bundle b = msg.getData();
                if (b.getInt("errorCode") == 0) {
                    if (b.getBoolean("favorite")) {
                        icon_favorite.setImageResource(R.drawable.ic_favorate_yellow);
                        icon_favorite.setTag("favor");
                    } else {
                        icon_favorite.setImageResource(R.drawable.ic_favorate_gray);
                        icon_favorite.setTag("unfavor");
                    }
                }
            }
        };
    }

    private void initView() {
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
        icon_favorite = findViewById(R.id.icon_favorate);
        icon_gender = findViewById(R.id.icon_gender);
        card_new = findViewById(R.id.card_new);
        card_cannot_bargain = findViewById(R.id.card_cannot_bargain);
        layout_seller = findViewById(R.id.layout_seller);
    }
}


