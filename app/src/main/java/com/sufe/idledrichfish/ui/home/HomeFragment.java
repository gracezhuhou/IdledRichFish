package com.sufe.idledrichfish.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.data.ProductDataSource;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

import android.widget.ImageView;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.sufe.idledrichfish.data.ProductRepository;
import com.sufe.idledrichfish.data.model.Product;
import com.sufe.idledrichfish.data.model.Student;

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
    private RollPagerView mRollViewPager;

    private GridLayoutManager layoutManager;
    private HomeRecyclerViewAdapter productsRecyclerAdapter;
    private List<Product> products;
    static public Handler homeProductsHandler;
    static public Handler homeStudentHandler;

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
        // Inflate the view for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ptrFrameLayout = view.findViewById(R.id.refreshLayout);
        mRecyclerView = view.findViewById(R.id.recyclerView_main);
        mRollViewPager = view.findViewById(R.id.roll_view_pager);

        setRecycler(); // 商品浏览
        setRefresh(); // 下拉刷新& 上拉加载
        setRoll(); // 图片轮播
        setHandler();

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

    /*
     * 商品展示
     */
    private void setRecycler() {
        products = new ArrayList<>();
        ProductRepository.getInstance(new ProductDataSource()).queryProductsForHome();
        layoutManager = new GridLayoutManager(this.getContext(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        productsRecyclerAdapter = new HomeRecyclerViewAdapter(products);
        mRecyclerView.setAdapter(productsRecyclerAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    /*
     * 下拉刷新的头部 & 上拉加载的底部
     */
    private void setRefresh() {
        //StoreHouse风格的头部实现
        StoreHouseHeader storeHouseHeader = new StoreHouseHeader(this.getContext());
        storeHouseHeader.setPadding(0,50,0,0);
        storeHouseHeader.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        storeHouseHeader.setTextColor(Color.WHITE);
        storeHouseHeader.initWithString("sufe loading"); // 只可英文，中文不可运行(添加时间)
        ptrFrameLayout.setHeaderView(storeHouseHeader);
        ptrFrameLayout.addPtrUIHandler(storeHouseHeader);

        StoreHouseHeader storeHouseFooter = new StoreHouseHeader(this.getContext());
        storeHouseFooter.setPadding(0,10,0,10);
        storeHouseFooter.setBackgroundColor(getResources().getColor(R.color.transparent));
        storeHouseFooter.setTextColor(getResources().getColor(R.color.colorPrimary));
        storeHouseFooter.initWithString("loading"); // 只可英文，中文不可运行(添加时间)
        ptrFrameLayout.setFooterView(storeHouseFooter);
        ptrFrameLayout.addPtrUIHandler(storeHouseFooter);

        //经典风格的头部实现，下拉箭头+时间
//        PtrClassicDefaultHeader ptrClassicDefaultHeader = new PtrClassicDefaultHeader(this.getContext());
//        ptrFrameLayout.setHeaderView(ptrClassicDefaultHeader);
//       PtrClassicDefaultFooter ptrClassicDefaultFooter = new PtrClassicDefaultFooter(this.getContext());
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
                // 更新商品
                ProductRepository.getInstance(new ProductDataSource()).queryProductsForHome();
                productsRecyclerAdapter.notifyDataSetChanged();
            }
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
    }

    /*
     * 图片轮播
     */
    private void setRoll() {
        //设置播放时间间隔
        mRollViewPager.setPlayDelay(6000);
        //设置透明度
        mRollViewPager.setAnimationDurtion(500);
        //设置适配器
        mRollViewPager.setAdapter(new TestNormalAdapter());

        //设置指示器（顺序依次）
        //自定义指示器图片
        //设置圆点指示器颜色
        //设置文字指示器
        //隐藏指示器
        //mRollViewPager.setHintView(new IconHintView(this, R.drawable.point_focus, R.drawable.point_normal));
        //mRollViewPager.setHintView(new ColorPointHintView(this, Color.YELLOW,Color.WHITE));
        //mRollViewPager.setHintView(new TextHintView(this));
        //mRollViewPager.setHintView(null);
    }

    /*
     * 获取Recycler所需商品数据
     */
    @SuppressLint("HandlerLeak")
    private void setHandler() {
        // 获取商品信息
        homeProductsHandler = new Handler() {
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
                        BmobFile image = new BmobFile();
                        image.setUrl(bundle.getString("productImage"));
                        product.setImage1(image);
                        products.add(product);
                        bundles.remove(String.valueOf(i));
                    }
                }
                Log.i("Handler", "Query All Products");
                productsRecyclerAdapter.notifyDataSetChanged();
            }
        };
        // 获取卖家信息
        homeStudentHandler = new Handler() {
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
    }

}

/*
 * Adapter: 图片轮播
 */
class TestNormalAdapter extends StaticPagerAdapter {
    private int[] imgs = {
            R.drawable.ic_book,
            R.drawable.ic_cosmetic,
            R.drawable.ic_clothes,
            R.drawable.ic_electronics,
    };

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        view.setImageResource(imgs[position]);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    public int getCount() {
        return imgs.length;
    }
}
