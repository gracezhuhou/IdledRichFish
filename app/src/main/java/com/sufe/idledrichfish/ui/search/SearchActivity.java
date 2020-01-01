package com.sufe.idledrichfish.ui.search;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.data.ProductDataSource;
import com.sufe.idledrichfish.data.ProductRepository;
import com.sufe.idledrichfish.data.TagDataSource;
import com.sufe.idledrichfish.data.TagRepository;
import com.sufe.idledrichfish.ui.home.ProductRecyclerViewAdapter;
import com.sufe.idledrichfish.ui.home.ProductView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recycler_view;
    private SearchView search_view;

    private ProductRecyclerViewAdapter productsRecyclerAdapter;
    private List<ProductView> products;
    static public Handler searchHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recycler_view = findViewById(R.id.recycler_view);
        search_view = findViewById(R.id.search_view);

        initData();
        setRecycler();
        setHandler();

    }

    private void initData() {
        products = new ArrayList<>();
        Intent intent = getIntent();
        String searchText = intent.getStringExtra("search_text");
        String category = intent.getStringExtra("category");
        if (searchText != null) {
            search_view.setQuery(searchText, false);
            TagRepository.getInstance(new TagDataSource()).queryProductsByTag(searchText);
        } else if (category != null) {
            search_view.setQuery(category, false);
            ProductRepository.getInstance(new ProductDataSource()).queryProductsByCategory(category);
        }
    }

    private void setRecycler() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recycler_view.setLayoutManager(layoutManager);
        productsRecyclerAdapter = new ProductRecyclerViewAdapter(products);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setAdapter(productsRecyclerAdapter);
        recycler_view.setHasFixedSize(true);
    }

    @SuppressLint("HandlerLeak")
    private void setHandler() {
        // 获取商品信息
        searchHandler = new Handler() {
            public void handleMessage(Message msg) {
                products.clear();
                Bundle bundles = msg.getData();
                if (bundles.getInt("errorCode") == 0) {
                    bundles.remove("errorCode");
                    for (int i = 0; !bundles.isEmpty(); ++i) {
                        Bundle bundle = bundles.getBundle(String.valueOf(i));
                        assert bundle != null;

                        ProductView product = new ProductView(
                                bundle.getString("objectId"),
                                bundle.getString("name"),
                                bundle.getDouble("price"),
                                bundle.getBoolean("isNew"),
                                bundle.getBoolean("canBargain"),
                                bundle.getByteArray("productImage"),
                                bundle.getString("sellerId"),
                                bundle.getString("sellerName"),
                                bundle.getFloat("credit"),
                                bundle.getByteArray("studentImage"));
                        products.add(product);
                        bundles.remove(String.valueOf(i));
                    }
                    Log.i("Handler", "Query All Products");
                    productsRecyclerAdapter.notifyDataSetChanged();
                } else {
                    // 9016 网络问题
                }
            }
        };

}}