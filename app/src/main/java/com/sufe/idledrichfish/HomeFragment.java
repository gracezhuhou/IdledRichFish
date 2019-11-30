package com.sufe.idledrichfish;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sufe.idledrichfish.database.Label;
import com.sufe.idledrichfish.database.LabelBLL;
import com.sufe.idledrichfish.database.Product;
import com.sufe.idledrichfish.database.ProductBLL;
import com.sufe.idledrichfish.database.ProductDAL;
import com.sufe.idledrichfish.database.Student;
import com.sufe.idledrichfish.database.StudentBLL;

import java.util.ArrayList;
import java.util.List;


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
    private String mParam2;

    private ProductBLL productBLL;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private ProductsRecyclerAdapter productsRecyclerAdapter;

    private OnFragmentInteractionListener mListener;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the dialog_price_publishment for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        productBLL = new ProductBLL();

//        insertExampleData(); // 需要有测试的数据时使用，第二次运行时请注释掉
//        LitePal.deleteAll("Product"); // 数据出问题闪退时使用

        List<Product> products = productBLL.getAllProducts();
        mRecyclerView = view.findViewById(R.id.recyclerView_main);
        layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        productsRecyclerAdapter = new ProductsRecyclerAdapter(products);
        mRecyclerView.setAdapter(productsRecyclerAdapter);
        mRecyclerView.setHasFixedSize(true);

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

    private void insertExampleData() {
        LabelBLL labelBLL = new LabelBLL();
        Label label = new Label();
        label.setName("高数");
        labelBLL.insertLabel(label);

        StudentBLL studentDAL = new StudentBLL();
        Student student = new Student();
        student.setStudentId("2017110001");
        student.setName("Simon");
        student.setPassword("123456");
        student.setGender("male");
        student.setAdminId(1);
        studentDAL.insertStudent(student);

        List<Label> labels = new ArrayList<>();
        labels.add(label);
        Product product = new Product();
        product.setName("高数练习册");
        product.setDescription("涨知识嘻嘻");
        product.setPublisherId("2017110001");
        product.setLabels(labels);
        product.setPrice(999);
        product.setCategory("书籍");
        productBLL.insertProduct(product);
    }
}

