package com.sufe.idledrichfish.ui.search;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.data.ProductDataSource;
import com.sufe.idledrichfish.data.ProductRepository;
import com.sufe.idledrichfish.data.model.Product;
import com.sufe.idledrichfish.data.model.Student;
import com.sufe.idledrichfish.ui.home.HomeRecyclerViewAdapter;
import com.sufe.idledrichfish.ui.myPublish.MyPublishRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

public class SearchActivity extends AppCompatActivity {
    private GridLayoutManager layoutManager;
    private RecyclerView recycler_view;
    private SearchRecyclerViewAdapter productsRecyclerAdapter;
    private List<Product> products;
    static public Handler searchHandler;
    static public Handler searchStudentHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        recycler_view = findViewById(R.id.recycler_view);

        setRecycler();
        setHandler();



    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search, container, false);
        recycler_view = view.findViewById(R.id.recycler_home);



        return view;

    }

    private void setRecycler() {
        products = new ArrayList<>();
        Intent intent = getIntent();
        String search_text = intent.getStringExtra("search_text");
        String list = intent.getStringExtra("list");

        ProductRepository.getInstance(new ProductDataSource()).queryListProducts(list);
        layoutManager = new GridLayoutManager(this, 2);
        recycler_view.setLayoutManager(layoutManager);
        productsRecyclerAdapter = new SearchRecyclerViewAdapter(products);
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
                        product.setNew(bundle.getBoolean("isNew"));
                        product.setPrice(bundle.getDouble("price"));
                        product.setCanBargain(bundle.getBoolean("canBargain"));
                        Student seller = new Student();
                        seller.setObjectId(bundle.getString("sellerId"));
                        product.setSeller(seller);
                        BmobFile image = new BmobFile("image1", "", bundle.getString("productImage"));
//                        image.setUrl(bundle.getString("productImage"));
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
        // 获取卖家信息
        searchStudentHandler = new Handler() {
            public void handleMessage(Message msg) {
                Bundle b = msg.getData();
                if (b.getInt("errorCode") == 0) {
                    Student student = new Student();
                    student.setObjectId(b.getString("id"));
                    student.setName(b.getString("name"));
                    student.setCredit(b.getFloat("credit"));
                    BmobFile bmobFile = new BmobFile();
                    bmobFile.setUrl(b.getString("image"));
                    student.setImage(bmobFile);
                    Log.i("Handler", "Query Student Success");

                    int position = b.getInt("position");
                    Product product = products.get(position);
                    product.setSeller(student);
                    products.set(position, product);
                    productsRecyclerAdapter.notifyItemChanged(position, student);
                }
                Log.i("Handler", "Query Student By Product");
//                productsRecyclerAdapter.notifyDataSetChanged();
            }
        };

}}