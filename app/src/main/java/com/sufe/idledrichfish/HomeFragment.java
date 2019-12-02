package com.sufe.idledrichfish;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sufe.idledrichfish.database.Label;
import com.sufe.idledrichfish.database.LabelBLL;
import com.sufe.idledrichfish.database.Product;
import com.sufe.idledrichfish.database.ProductBLL;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultFooter;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TO DO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TO DO: Rename and change types of parameters
    private String mParam1;

    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private PtrFrameLayout ptrFrameLayout;

    private LinearLayoutManager layoutManager;
    private ProductsRecyclerAdapter productsRecyclerAdapter;

    private ProductBLL productBLL;
    private List<Product> products;

    static public Handler myHandler;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param param1 Parameter 1.
     * @return A new instance of fragment HomeFragment.
     */
    // TO DO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

        // Recycler局部刷新线程
        myHandler = new Handler() {
            public void handleMessage(Message msg) {
                Bundle b = msg.getData();
                String studentId = b.getString("studentId");

                Log.i("Handler", "局部刷新界面");
                // TODO:刷新界面
                productsRecyclerAdapter.notifyDataSetChanged();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the dialog_price_publishment for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        productBLL = new ProductBLL();

        deleteAllData(); // 数据出问题闪退时使用
        insertExampleData(); // 需要有测试的数据时使用，第二次运行时请注释掉

        ptrFrameLayout = view.findViewById(R.id.refreshLayout);
        mRecyclerView = view.findViewById(R.id.recyclerView_main);

        setRecycler();
        setRefresh();


        return view;
    }

    // TO DO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        // TO DO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setRecycler() {
        products = productBLL.getAllProducts();
        layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        productsRecyclerAdapter = new ProductsRecyclerAdapter(products);
        mRecyclerView.setAdapter(productsRecyclerAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    private void setRefresh() {
        //StoreHouse风格的头部实现
        StoreHouseHeader storeHouseHeader = new StoreHouseHeader(getContext());
        storeHouseHeader.setPadding(0,50,0,0);
        storeHouseHeader.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        storeHouseHeader.setTextColor(Color.WHITE);
        storeHouseHeader.initWithString("sufe loading"); // 只可英文，中文不可运行(添加时间)
        ptrFrameLayout.setHeaderView(storeHouseHeader);
        ptrFrameLayout.addPtrUIHandler(storeHouseHeader);

        StoreHouseHeader storeHouseFooter = new StoreHouseHeader(getContext());
        storeHouseFooter.setPadding(0,10,0,10);
        storeHouseFooter.setBackgroundColor(getResources().getColor(R.color.transparent));
        storeHouseFooter.setTextColor(getResources().getColor(R.color.colorPrimary));
        storeHouseFooter.initWithString("loading"); // 只可英文，中文不可运行(添加时间)
        ptrFrameLayout.setFooterView(storeHouseFooter);
        ptrFrameLayout.addPtrUIHandler(storeHouseFooter);

        //经典风格的头部实现，下拉箭头+时间
//        PtrClassicDefaultHeader ptrClassicDefaultHeader = new PtrClassicDefaultHeader(getContext());
//        ptrFrameLayout.setHeaderView(ptrClassicDefaultHeader);
//       PtrClassicDefaultFooter ptrClassicDefaultFooter = new PtrClassicDefaultFooter(getContext());
//        ptrFrameLayout.setFooterView(ptrClassicDefaultFooter);
//        ptrFrameLayout.addPtrUIHandler(ptrClassicDefaultHeader);
//       ptrFrameLayout.addPtrUIHandler(ptrClassicDefaultFooter);

        // 监听刷新过程
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                frame.postDelayed(ptrFrameLayout::refreshComplete, 2000);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(ptrFrameLayout::refreshComplete, 2000);
                products.clear();
                products.addAll(productBLL.getAllProducts());
                productsRecyclerAdapter.notifyDataSetChanged();
            }
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
    }

    private void insertExampleData() {
        LabelBLL labelBLL = new LabelBLL();
        Label label = new Label();
        label.setName("高数");
        labelBLL.insertLabel(label);

//        StudentBLL studentDAL = new StudentBLL();
//        Student student = new Student();
//        student.setStudentNumber("2017110001");
//        student.setName("Simon");
//        student.setGender("male");
//        studentDAL.insertStudent(student);

        List<Label> labels = new ArrayList<>();
        labels.add(label);
        Product product = new Product();
        product.setName("高数练习册");
        product.setDescription("涨知识嘻嘻");
        product.setPublisherId("a41b6f2562");
        product.setLabels(labels);
        product.setPrice(999);
        product.setCategory("书籍");
        productBLL.insertProduct(product);
    }

    private void deleteAllData(){
        LitePal.deleteAll("Product");
        LitePal.deleteAll("Student");
        LitePal.deleteAll("Label");
    }
}

