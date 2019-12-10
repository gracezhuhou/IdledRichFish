package com.sufe.idledrichfish.ui.myFavorite;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.data.FavoriteDataSource;
import com.sufe.idledrichfish.data.FavoriteRepository;
import com.sufe.idledrichfish.data.ProductDataSource;
import com.sufe.idledrichfish.data.ProductRepository;
import com.sufe.idledrichfish.data.model.Product;
import com.sufe.idledrichfish.data.model.Student;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

public class MyFavoriteActivity extends AppCompatActivity {

    private RecyclerView recycler_view;

    private LinearLayoutManager layoutManager;
    private MyFavoriteRecyclerViewAdapter productsRecyclerAdapter;
    private List<Product> products;
    static public Handler myFavoriteHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);

        recycler_view = findViewById(R.id.recycler_view);

        setRecycler();
        setHandler();
    }

    private void setRecycler() {
        products = new ArrayList<>();
        FavoriteRepository.getInstance(new FavoriteDataSource()).queryMyFavorite();
        layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);
        productsRecyclerAdapter = new MyFavoriteRecyclerViewAdapter(products);
        recycler_view.setAdapter(productsRecyclerAdapter);
        recycler_view.setHasFixedSize(true);
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
