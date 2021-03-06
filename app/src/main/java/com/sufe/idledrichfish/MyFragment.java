package com.sufe.idledrichfish;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.sufe.idledrichfish.data.LoginDataSource;
import com.sufe.idledrichfish.data.LoginRepository;
import com.sufe.idledrichfish.data.model.Student;
import com.sufe.idledrichfish.ui.login.LoginActivity;
import com.sufe.idledrichfish.ui.myFavorite.MyFavoriteActivity;
import com.sufe.idledrichfish.ui.myHistory.MyHistoryActivity;
import com.sufe.idledrichfish.ui.myOrder.MyOrderActivity;
import com.sufe.idledrichfish.ui.myPublish.MyPublishActivity;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment {
    // Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";

    // Rename and change types of parameters
    private String mParam1;

    private OnFragmentInteractionListener mListener;

    private CircleImageView image_student;
    private TextView text_stu_name;
    private TextView text_stu_number;
    private LinearLayout layout_my_info;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;

    public MyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param param1 Parameter 1.
     * @return A new instance of fragment MyFragment.
     */
    // Rename and change types and number of parameters
    public static MyFragment newInstance(String param1, String param2) {
        MyFragment fragment = new MyFragment();
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
        // Inflate the layout for this fragment
        super.onActivityCreated(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        text_stu_name = view.findViewById(R.id.text_name);
        text_stu_number = view.findViewById(R.id.text_stuNumber);
        image_student = view.findViewById(R.id.image_student);
        layout_my_info = view.findViewById(R.id.layout_my_info);
        toolbar = view.findViewById(R.id.toolbar);
        appBarLayout = view.findViewById(R.id.app_bar);

        setAppBar();
        initData(); // 显示账户信息

        // 点击登出按钮
        final Button button_log_out = view.findViewById(R.id.button_log_out);
        button_log_out.setOnClickListener(view1 -> logOut());
        // 点击“我的发布”
        final Button button_my_publish = view.findViewById(R.id.button_my_publish);
        button_my_publish.setOnClickListener(view1 -> {
            // 跳转至MyPublishActivity
            Intent intent = new Intent(getContext(), MyPublishActivity.class);
            startActivity(intent);
        });
        // 点击“我的收藏”
        final Button button_my_favorite = view.findViewById(R.id.button_my_favorite);
        button_my_favorite.setOnClickListener(view1 -> {
            // 跳转至MyFavoriteActivity
            Intent intent = new Intent(getContext(), MyFavoriteActivity.class);
            startActivity(intent);
        });
        // 点击“我的订单”
        final Button button_my_order = view.findViewById(R.id.button_my_order);
        button_my_order.setOnClickListener(view1 -> {
            // 跳转至MyOrderActivity
            Intent intent = new Intent(getContext(), MyOrderActivity.class);
            startActivity(intent);
        });
        // 点击“我的足迹”
        final Button button_my_history = view.findViewById(R.id.button_my_history);
        button_my_history.setOnClickListener(view1 -> {
            // 跳转至MyHistoryActivity
            Intent intent = new Intent(getContext(), MyHistoryActivity.class);
            startActivity(intent);
        });

        return view;
    }

    // Rename method, update argument and hook method into UI event
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
        // Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * 初始化数据，显示用户信息
     */
    private void initData() {
        Student student = Student.getCurrentUser(Student.class);
        text_stu_name.setText(student.getName());
        String numberText = "No." + student.getUsername();
        text_stu_number.setText(numberText);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_no_image) // 图片加载出来前，显示的图片
                .fallback(R.drawable.head) // url为空的时候,显示的图片
                .error(R.drawable.ic_fail); // 图片加载失败后，显示的图片
        Glide.with(this).load(student.getImage()).apply(options).into(image_student);
    }

    /**
     * 设置AppBar
     * AppBar的折叠效果
     */
    private void setAppBar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                Log.d("STATE", state.name());
                if( state == State.EXPANDED ) {
                    //展开状态
                    layout_my_info.setVisibility(View.VISIBLE);
                }else if(state == State.COLLAPSED){
                    //折叠状态
                    layout_my_info.setVisibility(View.INVISIBLE);
                }else {
                    //中间状态
                    layout_my_info.setVisibility(View.VISIBLE);
                }
            }
        });
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                float alpha = ((float)128 + (float)i) / (float)128; // todo:128?
                layout_my_info.setAlpha(alpha);
                Log.i("AppBar", String.valueOf(i));
            }
        });
    }


    /**
     * 点击登出按钮
     */
    private void logOut() {
        // 登出
        LoginRepository.getInstance(new LoginDataSource()).logout();
        // 跳转至登录界面
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }
}

