package com.sufe.idledrichfish.ui.myFavorite;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.data.FavoriteDataSource;
import com.sufe.idledrichfish.data.FavoriteRepository;
import com.sufe.idledrichfish.data.model.Product;
import com.sufe.idledrichfish.data.model.Student;

import java.util.ArrayList;
import java.util.List;

public class MyFavoriteActivity extends AppCompatActivity {

    private LinearLayoutManager layoutManager;
    private MyFavoriteRecyclerViewAdapter productsRecyclerAdapter;
    private List<Product> products;
    static public Handler myFavoriteHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);

        setRecycler();
        setHandler();
        setToolbar();
    }

    private void setRecycler() {
        final RecyclerView recycler_view = findViewById(R.id.recycler_view);
        products = new ArrayList<>();
        FavoriteRepository.getInstance(new FavoriteDataSource()).queryMyFavorite();
        layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);
        productsRecyclerAdapter = new MyFavoriteRecyclerViewAdapter(products);
        recycler_view.setAdapter(productsRecyclerAdapter);
        recycler_view.setHasFixedSize(true);
    }

    /**
     * 设置Toolbar
     */
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

    @SuppressLint("HandlerLeak")
    private void setHandler() {
        // 获取用户发布的所以商品
        myFavoriteHandler = new Handler() {
            public void handleMessage(Message msg) {
                products.clear();
                Bundle bundles = msg.getData();
                if (bundles.getInt("errorCode") == 0) {
                    bundles.remove("errorCode");
                    if (bundles.isEmpty()) {
                        productsRecyclerAdapter.notifyDataSetChanged();
                        return;
                    }
                    for (int i = 0; !bundles.isEmpty(); ++i) {
                        Bundle b = bundles.getBundle(String.valueOf(i));
                        assert b != null;
                        Product product = new Product();
                        product.setObjectId(b.getString("productId"));
                        product.setName(b.getString("name"));
                        product.setPrice(b.getDouble("price"));
                        Student seller = new Student();
                        seller.setObjectId(b.getString("sellerId"));
                        seller.setName(b.getString("sellerName"));
                        seller.setCredit(b.getFloat("credit"));
//                        // todo: image
                        product.setSeller(seller);
                        products.add(product);
                        bundles.remove(String.valueOf(i));
                    }
                    Log.i("Handler", "Query All Favorite Products");
                    productsRecyclerAdapter.notifyDataSetChanged();
                } else {
                    // 9016 网络问题
                }
            }
        };
    }
}
