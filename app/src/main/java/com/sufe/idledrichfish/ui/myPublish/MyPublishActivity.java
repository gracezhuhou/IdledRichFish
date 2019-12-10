package com.sufe.idledrichfish.ui.myPublish;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.data.ProductDataSource;
import com.sufe.idledrichfish.data.ProductRepository;
import com.sufe.idledrichfish.data.model.Product;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

public class MyPublishActivity extends AppCompatActivity {

    private RecyclerView recycler_view;

    private LinearLayoutManager layoutManager;
    private MyPublishRecyclerViewAdapter productsRecyclerAdapter;
    private List<Product> products;
    static public Handler myPublishHandler;
    static public Handler myPublishDeleteHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_publish);

        recycler_view = findViewById(R.id.recycler_view);

        setRecycler();
        setHandler();
    }

    private void setRecycler() {
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
        // 获取用户发布的所以商品
        myPublishHandler = new Handler() {
            public void handleMessage(Message msg) {
                Bundle bundles = msg.getData();
                if (bundles.getInt("errorCode") == 0) {
                    bundles.remove("errorCode");
                    if (bundles.isEmpty()) {
                        productsRecyclerAdapter.notifyDataSetChanged();
                        return;
                    }
                    for (int i = 0; !bundles.isEmpty(); ++i) {
                        Bundle bundle = bundles.getBundle(String.valueOf(i));
                        assert bundle != null;
                        Product product = new Product();
                        product.setObjectId(bundle.getString("objectId"));
                        product.setName(bundle.getString("name"));
                        product.setPrice(bundle.getDouble("price"));
                        BmobFile image = new BmobFile();
                        // todo: image
                        product.setImage1(image);
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

        // 获取Bmob返回的信息：删除发布商品
        myPublishDeleteHandler = new Handler() {
            public void handleMessage(Message msg) {
                int errorCode = msg.getData().getInt("errorCode");
                Log.i("Handler", "Error Code " + errorCode);

                if (errorCode == 0) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MyPublishActivity.this)
                            .setTitle("商品删除成功")
                            .setIcon(R.drawable.ic_audit)
                            .create();
                    alertDialog.show();
                }
                else {
                    String fail = "失败原因:" + errorCode + msg.getData().getString("e");

                    AlertDialog alertDialog = new AlertDialog.Builder(MyPublishActivity.this)
                            .setTitle("商品删除失败")
                            .setMessage(fail)
                            .setIcon(R.drawable.ic_fail)
                            .create();
//                    if (e.getErrorCode() == 202)
//                        alertDialog.setMessage("");
//                    else if (e.getErrorCode() == 301)
//                        alertDialog.setMessage("");
                    alertDialog.show();
                }
            }
        };
    }
}
