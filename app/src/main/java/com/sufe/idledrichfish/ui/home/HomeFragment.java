package com.sufe.idledrichfish.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.ui.search.SearchActivity;
import com.sufe.idledrichfish.data.ProductDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.bmob.v3.datatype.BmobFile;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.sufe.idledrichfish.data.ProductRepository;
import com.sufe.idledrichfish.data.model.Product;
import com.sufe.idledrichfish.data.model.Student;
import com.sufe.idledrichfish.ui.publish.PublishActivity;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // Rename and change types of parameters
    private String mParam1;

    private OnFragmentInteractionListener mListener;

    private SearchBox search_box;
    private RecyclerView mRecyclerView;
    private PtrFrameLayout layout_refresh;
    private RollPagerView mRollViewPager;
    private ImageView icon_search;
    private ImageView icon_publish;
    private Button imageButton_catogory_book;

    private GridLayoutManager layoutManager;
    private HomeRecyclerViewAdapter productsRecyclerAdapter;
    private List<Product> products;
    static public Handler homeProductsHandler;
    static public Handler homeStudentHandler;

    private Context mContext;
    private SearchBox sbSearch;
    private TextView tvBottom;
    private EditText search_text;
    //数据列表
    private List<Product> listSearch;
    //结果列表
    private List<Product> listResult;
    private ListView mSearchResult ;
    private SearchBox.SearchAdapter mResultAdapter ;

    private ListView mHistory ;
    //private SearchBox.HistoryAdapter mHistoryAdapter;



    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param param1 Parameter 1.
     * @return A new instance of fragment HomeFragment.
     */
    // Rename and change types and number of parameters
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

        //search_box = view.findViewById(R.id.search_box);
        //mSearchResult = view.findViewById(R.id.content_list_search);
        layout_refresh = view.findViewById(R.id.layout_refresh);
        mRecyclerView = view.findViewById(R.id.recycler_home);
        mRollViewPager = view.findViewById(R.id.roll_view_pager);
        icon_search = view.findViewById(R.id.icon_search);
        icon_publish = view.findViewById(R.id.icon_publish);
        imageButton_catogory_book = view.findViewById(R.id.imageButton_catogory_book);
        search_text = view.findViewById(R.id.search_text);

        setRecycler(); // 商品浏览
        setRefresh(); // 下拉刷新& 上拉加载
        setRoll(); // 图片轮播
        setSearch(); // 搜索框
        setHandler();

        // 点击发布按钮
        icon_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PublishActivity.class);
                startActivity(intent);
            }
        });

        //点击搜索按钮
        icon_search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                search_text = view.findViewById(R.id.search_text);

                String searchtext= search_text.getText().toString();
                Intent intent = new Intent(getContext(), PublishActivity.class);
                intent.putExtra("search_text",searchtext);
                startActivity(intent);
            }
        });

        //点击书籍图片按钮
        imageButton_catogory_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 跳转至搜索页面
                String list = "book";
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("list",list);
                startActivity(intent);
            }
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

    @Override
    public void onResume() {
        search_box.post(new Runnable() {
            @Override
            public void run() {
                search_box.hideCircularlyToMenuItem(R.id.icon_search, Objects.requireNonNull(getActivity()));
            }
        });
//        search_box.hideCircularlyToMenuItem(R.id.icon_search, Objects.requireNonNull(getActivity()));
        super.onResume();
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

    /**
     * 商品展示
     */
    private void setRecycler() {
        products = new ArrayList<>();
        ProductRepository.getInstance(new ProductDataSource()).queryProductsForHome(false);
        layoutManager = new GridLayoutManager(this.getContext(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        productsRecyclerAdapter = new HomeRecyclerViewAdapter(products);
        mRecyclerView.setAdapter(productsRecyclerAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    /**
     * 下拉刷新的头部 & 上拉加载的底部
     */
    private void setRefresh() {
        //StoreHouse风格的头部实现
        StoreHouseHeader storeHouseHeader = new StoreHouseHeader(this.getContext());
        storeHouseHeader.setPadding(0,50,0,0);
        storeHouseHeader.setBackgroundColor(getResources().getColor(R.color.primary_dark));
        storeHouseHeader.setTextColor(Color.WHITE);
        storeHouseHeader.initWithString("sufe loading"); // 只可英文，中文不可运行(添加时间)
        layout_refresh.setHeaderView(storeHouseHeader);
        layout_refresh.addPtrUIHandler(storeHouseHeader);

        StoreHouseHeader storeHouseFooter = new StoreHouseHeader(this.getContext());
        storeHouseFooter.setPadding(0,10,0,10);
        storeHouseFooter.setBackgroundColor(getResources().getColor(R.color.transparent));
        storeHouseFooter.setTextColor(getResources().getColor(R.color.primary));
        storeHouseFooter.initWithString("loading"); // 只可英文，中文不可运行(添加时间)
        layout_refresh.setFooterView(storeHouseFooter);
        layout_refresh.addPtrUIHandler(storeHouseFooter);

        //经典风格的头部实现，下拉箭头+时间
//        PtrClassicDefaultHeader ptrClassicDefaultHeader = new PtrClassicDefaultHeader(this.getContext());
//        layout_refresh.setHeaderView(ptrClassicDefaultHeader);
//       PtrClassicDefaultFooter ptrClassicDefaultFooter = new PtrClassicDefaultFooter(this.getContext());
//        layout_refresh.setFooterView(ptrClassicDefaultFooter);
//        layout_refresh.addPtrUIHandler(ptrClassicDefaultHeader);
//       layout_refresh.addPtrUIHandler(ptrClassicDefaultFooter);

        // 监听刷新过程
        layout_refresh.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                frame.postDelayed(layout_refresh::refreshComplete, 2000);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(layout_refresh::refreshComplete, 2000);
                // 更新商品
                ProductRepository.getInstance(new ProductDataSource()).queryProductsForHome(true);
                productsRecyclerAdapter.notifyDataSetChanged();
            }
        });
        layout_refresh.setMode(PtrFrameLayout.Mode.BOTH);
    }

    /**
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

    /**
     * 搜索框
     */
    private void initData() {
        //本地可供检索数据获取,每次resume就要重新渲染
        listSearch = new ArrayList<>();
        ProductRepository.getInstance(new ProductDataSource()).queryProductsForHome(false);
    }


    private void setSearch() {
        search_box.enableVoiceRecognition(this);
        search_box.post(new Runnable() {
            @Override
            public void run() {
                search_box.hideCircularly( Objects.requireNonNull(getActivity()));
            }
        });

        for(int x = 0; x < 10; x++){
            SearchResult option = new SearchResult("Result " + Integer.toString(x), getResources().getDrawable(R.drawable.ic_history));
            search_box.addSearchable(option);
        }

        icon_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_box.revealFromMenuItem(R.id.icon_search, Objects.requireNonNull(getActivity()));
            }
        });

        search_box.setMenuListener(new SearchBox.MenuListener(){

            @Override
            public void onMenuClick() {
                //Hamburger has been clicked
                Toast.makeText(getContext(), "Menu click", Toast.LENGTH_LONG).show();
            }

        });

        search_box.setSearchListener(new SearchBox.SearchListener(){

            @Override
            public void onSearchOpened() {
                //Use this to tint the screen
                Toast.makeText(getContext(), " Search Open", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onSearchClosed() {
                //Use this to un-tint the screen
                Toast.makeText(getContext(),  "Search Close", Toast.LENGTH_LONG).show();
                search_box.hideCircularlyToMenuItem(R.id.icon_search, Objects.requireNonNull(getActivity()));
            }

            @Override
            public void onSearchTermChanged(String searchTerm) {
                //React to the search term changing
                //Called after it has updated results
                search(searchTerm);
            }

            @Override
            public void onSearch(String searchTerm) {
                Toast.makeText(getContext(), searchTerm +" Searched", Toast.LENGTH_LONG).show();
                search(searchTerm);
            }

            @Override
            public void onResultClick(SearchResult result){
                //React to a result being clicked
                Toast.makeText(getContext(), "SearchResult Click", Toast.LENGTH_LONG).show();
            }


            @Override
            public void onSearchCleared() {
                Toast.makeText(getContext(), "Search Clear", Toast.LENGTH_LONG).show();
            }

        });
    }
    private void search(String newText){
        //若搜索内容为空
        if(newText.isEmpty()){
            listResult.clear();
        }
        else{

            for (Product product : listSearch) {//开始搜索
                //搜索内容搜索到相关
                if (product.getName().contains(newText) || product.getDescription().contains(newText)) {

                    if(listResult.indexOf(product)==-1) {//且 结果集内不含有此内容
                        listResult.add(product);
                    }
                }else{
                    //搜索内容搜索不到相关 检测是否之前有加入结果集 有则删除
                    if(listResult.indexOf(product)!=-1) {
                        listResult.remove(product);
                    }
                }
            }
        }
        mResultAdapter.notifyDataSetChanged();
    }


    /**
     * 获取RecyclerView所需商品数据
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

    private void goToActivity(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
    }

}

/**
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


