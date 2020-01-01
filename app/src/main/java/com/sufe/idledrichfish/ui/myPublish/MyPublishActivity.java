package com.sufe.idledrichfish.ui.myPublish;

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
import com.sufe.idledrichfish.data.ProductDataSource;
import com.sufe.idledrichfish.data.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class MyPublishActivity extends AppCompatActivity {

    private LinearLayoutManager layoutManager;
    private MyPublishRecyclerViewAdapter productsRecyclerAdapter;
    private List<MyPublishProductView> products;
    static public Handler myPublishHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_publish);

        setRecycler();
        setHandler();
        setToolbar();
    }

    private void setRecycler() {
        final RecyclerView recycler_view = findViewById(R.id.recycler_view);
        products = new ArrayList<>();
        ProductRepository.getInstance(new ProductDataSource()).queryMyPublishProducts();
        layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);
        productsRecyclerAdapter = new MyPublishRecyclerViewAdapter(products);
        recycler_view.setAdapter(productsRecyclerAdapter);
        recycler_view.setHasFixedSize(true);
    }

    @SuppressLint("HandlerLeak")
    private void setHandler() {
        // 获取用户发布的所有商品
        myPublishHandler = new Handler() {
            public void handleMessage(Message msg) {
                Bundle bs = msg.getData();
                if (bs.getInt("errorCode") == 0) {
                    bs.remove("errorCode");
                    for (int i = 0; !bs.isEmpty(); ++i) {
                        Bundle b = bs.getBundle(String.valueOf(i));
                        assert b != null;
                        MyPublishProductView product = new MyPublishProductView(
                                b.getString("objectId"),
                                b.getString("name"),
                                b.getDouble("price"),
                                b.getByteArray("image1"),
                                b.getByteArray("image2"),
                                b.getByteArray("image3"),
                                b.getByteArray("image4"));
                        products.add(product);
                        bs.remove(String.valueOf(i));
                    }
                    Log.i("Handler", "Query All Products");
                    productsRecyclerAdapter.notifyDataSetChanged();
                } else {
                    // 9016 网络问题
                }
            }
        };
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
}
