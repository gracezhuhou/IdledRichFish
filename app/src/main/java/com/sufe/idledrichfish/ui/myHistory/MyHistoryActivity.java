package com.sufe.idledrichfish.ui.myHistory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.data.StudentDataSource;
import com.sufe.idledrichfish.data.StudentRepository;
import com.sufe.idledrichfish.ui.myFavorite.FavoriteProductView;
import com.sufe.idledrichfish.ui.myFavorite.MyFavoriteRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyHistoryActivity extends AppCompatActivity {

    private MyFavoriteRecyclerViewAdapter productsRecyclerAdapter;
    private List<FavoriteProductView> products;
    static public Handler historyHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_history);

        setRecycler();
        setHandler();
        setToolbar();
    }

    private void setRecycler() {
        final RecyclerView recycler_view = findViewById(R.id.recycler_view);
        products = new ArrayList<>();
        StudentRepository.getInstance(new StudentDataSource()).queryHistoryProducts();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
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
        // 获取用户足迹
        historyHandler = new Handler() {
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
                        products.add(new FavoriteProductView(
                                b.getString("productId"),
                                b.getString("name"),
                                b.getDouble("price"),
                                b.getString("sellerId"),
                                b.getString("sellerName"),
                                b.getFloat("credit"),
                                b.getByteArray("image1"),
                                b.getByteArray("image2"),
                                b.getByteArray("image3"),
                                b.getByteArray("image4"),
                                b.getByteArray("sellerImage")));
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
