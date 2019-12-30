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

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private RecyclerView recycler_view;

    private static final String ARG_SECTION_NUMBER = "section_number";
    private List<MyOrderView> orders = new ArrayList<>();
    private OrderRepository orderRepository = OrderRepository.getInstance(new OrderDataSource());
    private MyOrdersRecyclerAdapter myOrdersRecyclerAdapter;
    private boolean isViewCreated = false; // 控件是否初始化完成
    private boolean isLoadDataCompleted = false; // 数据是否已加载完毕
    static public Handler orderHandler1;
    static public Handler orderHandler2;
    static public Handler orderHandler3;

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
        Log.i("MyOrder","onCreate");
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        setHandler(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_order, container, false);
        isViewCreated = true;

        recycler_view = root.findViewById(R.id.recycler_view);
        setRecycler();

        return root;
    }

    // 懒加载机制
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i("MyOrder","setUserVisibleHint" + isVisibleToUser);
        if (isVisibleToUser && isViewCreated && !isLoadDataCompleted) {
            isLoadDataCompleted = true;
            loadData();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint()) {
            isLoadDataCompleted = true;
            loadData();
        }
    }

    private void setRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler_view.setLayoutManager(layoutManager);
        myOrdersRecyclerAdapter = new MyOrdersRecyclerAdapter(orders);
        recycler_view.setAdapter(myOrdersRecyclerAdapter);
        recycler_view.setHasFixedSize(true);
    }

    @SuppressLint("HandlerLeak")
    private void setHandler(int index) {
        switch (index){
            case 1:
                // 获取用户发布的所有商品
                orderHandler1 = new Handler() {
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
                                        b.getByteArray("productImage"),
                                        b.getString("sellerId"),
                                        b.getString("sellerName"),
                                        b.getByteArray("sellerImage"));
                                orders.add(order);
                                bs.remove(String.valueOf(i));
                            }
                            Log.i("Handler", "Query All Orders ");
                            myOrdersRecyclerAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), bs.getString("e"), Toast.LENGTH_LONG).show();
                            // 9016 网络问题
                        }
                    }
                };
                break;
            case 2:
                // 获取用户发布的所有商品
                orderHandler2 = new Handler() {
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
                                        b.getByteArray("productImage"),
                                        b.getString("sellerId"),
                                        b.getString("sellerName"),
                                        b.getByteArray("sellerImage"));
                                orders.add(order);
                                bs.remove(String.valueOf(i));
                            }
                            Log.i("Handler", "Query All Orders ");
                            myOrdersRecyclerAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), bs.getString("e"), Toast.LENGTH_LONG).show();
                            // 9016 网络问题
                        }
                    }
                };
                break;
            case 3:
                // 获取用户发布的所有商品
                orderHandler3 = new Handler() {
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
                                        b.getByteArray("productImage"),
                                        b.getString("sellerId"),
                                        b.getString("sellerName"),
                                        b.getByteArray("sellerImage"));
                                orders.add(order);
                                bs.remove(String.valueOf(i));
                            }
                            Log.i("Handler", "Query All Orders ");
                            myOrdersRecyclerAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), bs.getString("e"), Toast.LENGTH_LONG).show();
                            // 9016 网络问题
                        }
                    }
                };
        }

    }

    // 根据index从Bmob获取订单数据
    private void loadData() {
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        Log.i("MyOrder", "Load Data, index: " + index);
        switch (index){
            case 1: orderRepository.queryOrders(-1); break; // 全部
            case 2: orderRepository.queryOrders(0); break; // 进行中
            case 3: orderRepository.queryOrders(1); // 待评价
        }
    }
}