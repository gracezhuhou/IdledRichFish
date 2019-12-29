package com.sufe.idledrichfish.ui.myOrder;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.widget.Toast;

import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.data.OrderDataSource;
import com.sufe.idledrichfish.data.OrderRepository;
import com.sufe.idledrichfish.ui.myPublish.MyPublishRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private RecyclerView recycler_view;

    private static final String ARG_SECTION_NUMBER = "section_number";
    private PageViewModel pageViewModel;
    private List<MyOrderView> orders = new ArrayList<>();
    private OrderRepository orderRepository = OrderRepository.getInstance(new OrderDataSource());
    private MyOrdersRecyclerAdapter myOrdersRecyclerAdapter;
    static public Handler orderHandler;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);

        Log.i("Order Tab Page", "index: " + index);
        setHandler();
        switch (index){
            case 1: orderRepository.queryOrders(-1); break; // 全部
            case 2: orderRepository.queryOrders(0); break; // 进行中
            case 3: orderRepository.queryOrders(1); break; // 待评价
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_order, container, false);
        final TextView textView = root.findViewById(R.id.section_label);
        recycler_view = root.findViewById(R.id.recycler_view);
        setRecycler();

        pageViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        pageViewModel.getOrders().observe(this, new Observer<List<MyOrderView>>() {
            @Override
            public void onChanged(@Nullable List<MyOrderView> os) {
                myOrdersRecyclerAdapter.notifyDataSetChanged();
            }
        });
        return root;
    }

    private void setRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler_view.setLayoutManager(layoutManager);
        myOrdersRecyclerAdapter = new MyOrdersRecyclerAdapter(orders);
        recycler_view.setAdapter(myOrdersRecyclerAdapter);
        recycler_view.setHasFixedSize(true);
        recycler_view.setNestedScrollingEnabled(false);
    }

    @SuppressLint("HandlerLeak")
    private void setHandler() {
        // 获取用户发布的所有商品
        orderHandler = new Handler() {
            public void handleMessage(Message msg) {
                orders.clear();
                Bundle bs = msg.getData();
                if (bs.getInt("errorCode") == 0) {
                    bs.remove("errorCode");
                    for (int i = 0; !bs.isEmpty(); ++i) {
                        Bundle b = bs.getBundle(String.valueOf(i));
                        assert b != null;
                        MyOrderView order = new MyOrderView(
                                b.getString("orderId"),
                                b.getInt("status"),
                                b.getString("productId"),
                                b.getString("productName"),
                                b.getDouble("price"),
                                b.getByteArray("image1"),
                                b.getString("sellerId"),
                                b.getString("sellerName"),
                                b.getByteArray("image4"));
                        orders.add(order);
                        bs.remove(String.valueOf(i));
                    }
                    Log.i("Handler", "Query All Orders");
                    // 设置ViewModel的Orders
//                    pageViewModel.setOrders(orders);
                    myOrdersRecyclerAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), bs.getString("e"), Toast.LENGTH_LONG).show();
                    // 9016 网络问题
                }
            }
        };
    }
}