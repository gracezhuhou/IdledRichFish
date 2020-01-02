package com.sufe.idledrichfish.ui.productinfo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.common.primitives.Bytes;
import com.sufe.idledrichfish.AppBarStateChangeListener;
import com.sufe.idledrichfish.MainActivity;
import com.sufe.idledrichfish.R;
import com.google.android.material.appbar.AppBarLayout;
import com.sufe.idledrichfish.data.CommentDataSource;
import com.sufe.idledrichfish.data.CommentRepository;
import com.sufe.idledrichfish.data.FavoriteDataSource;
import com.sufe.idledrichfish.data.FavoriteRepository;
import com.sufe.idledrichfish.data.ProductDataSource;
import com.sufe.idledrichfish.data.ProductRepository;
import com.sufe.idledrichfish.data.model.Student;
import com.sufe.idledrichfish.data.StudentDataSource;
import com.sufe.idledrichfish.data.StudentRepository;
import com.sufe.idledrichfish.ui.user.UserActivity;
import com.sufe.idledrichfish.ui.conversation.ConversationActivity;
import com.sufe.idledrichfish.data.model.Comment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductInfoActivity extends AppCompatActivity {

    private TextView text_product_name;
    private TextView text_price;
    private TextView text_old_price;
    private TextView text_product_description;
    private TextView text_seller_name;
    private TextView text_seller_credit;
    private TextView text_login_date;
    private TextView text_publish_date;
    private ImageView image_product;
    private ImageView image_seller;
    private ImageView icon_gender;
    private ImageView icon_favorite;
    private TextView text_new;
    private TextView text_cannot_bargain;
    private ConstraintLayout layout_seller;

    private String productId;
    private String sellerId;
    private String sellerName;
    private String commentContent;
    static public Handler productInfoHandler;
    static public Handler addFavoriteHandler;
    static public Handler cancelFavoriteHandler;
    static public Handler isFavoriteHandler;
    static public Handler commentHandler;
    static public Handler productCommentHandler;
    static public Handler replyHandler;

    private CommentExpandableListView commentExpandableListView;
    private ExpandableAdapter expandableAdapter;
    private List<CommentView> commentList = new ArrayList<>();

    private InputMethodManager inputMethodManager;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        initView();
        setAppBar();
        initData();
        setHandler();
        clickSeller();
        initExpandableListView();

        StudentRepository.getInstance(new StudentDataSource()).addHistory(productId);

        // 点击“收藏”
        final LinearLayout layout_favorite = findViewById(R.id.layout_favorite);
        layout_favorite.setOnClickListener(view -> clickFavorite());
        // 点击“联系卖家”
        final Button button_message = findViewById(R.id.button_message);
        button_message.setOnClickListener(view -> chat());
        // 点击“评论”
        final LinearLayout layout_comment = findViewById(R.id.layout_comment);
        layout_comment.setOnClickListener(view -> comment());

    }

    /**
     * 设置顶部AppBar
     * 折叠效果
     * Toolbar返回键监听
     */
    private void setAppBar() {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);   // 有返回箭头
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        }

        final AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        // 折叠状态监听
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                Log.d("STATE", state.name());
                if( state == State.EXPANDED ) {
                    //展开状态
                    layout_seller.setVisibility(View.VISIBLE);
                }else if(state == State.COLLAPSED){
                    //折叠状态
                    layout_seller.setVisibility(View.INVISIBLE);
                }else {
                    //中间状态
                    layout_seller.setVisibility(View.VISIBLE);
                    getSupportActionBar().setIcon(null);
                }
            }
        });
        // 折叠时淡出效果
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                float alpha = ((float)98 + (float)i) / (float)98;
                layout_seller.setAlpha(alpha);
                Log.i("AppBar", String.valueOf(i));
            }
        });

        // 返回键监听
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    /**
     * 点击用户跳转至用户界面
     */
    private void clickSeller() {
        final ConstraintLayout layout_seller = findViewById(R.id.layout_seller);
        layout_seller.setOnClickListener(view -> {
//            Intent intent = new Intent(getApplicationContext(), UserActivity.class);
//            intent.putExtra("seller_id_extra", sellerId);
//            intent.putExtra("seller_name_extra", sellerName);
//            startActivity(intent);
        });
    }

    /**
     * 初始化界面: Product, Favorite
     * 根据传入的product_id
     */
    private void initData() {
        Intent intent = getIntent();
        productId = intent.getStringExtra("product_id_extra");
        ProductRepository.getInstance(new ProductDataSource()).queryProduct(productId, "productInfo");
        FavoriteRepository.getInstance(new FavoriteDataSource()).isFavorite(productId);
        CommentRepository.getInstance(new CommentDataSource()).queryCommentsByProduct(productId);
    }

    /**
     * 根据Bmob传回的数据
     * 设定界面
     * 反馈收藏结果
     */
    @SuppressLint("HandlerLeak")
    private void setHandler() {
        // 获取商品信息 设定界面
        productInfoHandler = new Handler() {
            public void handleMessage(Message msg) {
                Bundle b = msg.getData();
                if (b.getInt("errorCode") == 0) {
                    // 商品信息
                    text_product_name.setText(b.getString("name"));
                    text_product_description.setText(b.getString("description"));
                    DecimalFormat format1 = new java.text.DecimalFormat("¥ 0.00"); // 保留小数点两位
                    text_price.setText(format1.format(b.getDouble("price")));
                    text_old_price.setText(format1.format(b.getDouble("oldPrice")));
                    if(!b.getBoolean("isNew")) {
                        text_new.setVisibility(View.GONE);
                    }
                    if (b.getBoolean("canBargain")) {
                        text_cannot_bargain.setVisibility(View.GONE);
                    }
                    String publishDate = "发布于" + b.getString("publishDate");
                    text_publish_date.setText(publishDate);
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.drawable.ic_no_image) // 图片加载出来前，显示的图片
                            .fallback(R.drawable.ic_no_image) // url为空的时候,显示的图片
                            .error(R.drawable.ic_fail); // 图片加载失败后，显示的图片
                    final ImageView image1_product = findViewById(R.id.image1_product);
                    Glide.with(getApplicationContext()).load(b.getByteArray("image1")).apply(options).into(image1_product);
                    final ImageView image2_product = findViewById(R.id.image2_product);
                    Glide.with(getApplicationContext()).load(b.getByteArray("image2")).into(image2_product);
                    final ImageView image3_product = findViewById(R.id.image3_product);
                    Glide.with(getApplicationContext()).load(b.getByteArray("image3")).into(image3_product);
                    final ImageView image4_product = findViewById(R.id.image4_product);
                    Glide.with(getApplicationContext()).load(b.getByteArray("image4")).into(image4_product);
                    final ImageView image5_product = findViewById(R.id.image5_product);
                    Glide.with(getApplicationContext()).load(b.getByteArray("image5")).into(image5_product);
                    final ImageView image6_product = findViewById(R.id.image6_product);
                    Glide.with(getApplicationContext()).load(b.getByteArray("image6")).into(image6_product);
                    final ImageView image7_product = findViewById(R.id.image7_product);
                    Glide.with(getApplicationContext()).load(b.getByteArray("image7")).into(image7_product);
                    final ImageView image8_product = findViewById(R.id.image8_product);
                    Glide.with(getApplicationContext()).load(b.getByteArray("image8")).into(image8_product);
                    final ImageView image9_product = findViewById(R.id.image9_product);
                    Glide.with(getApplicationContext()).load(b.getByteArray("image9")).into(image9_product);

                    // 卖家信息
                    sellerId = b.getString("sellerId");
                    sellerName = b.getString("sellerName");
                    text_seller_name.setText(sellerName);
                    DecimalFormat format2 = new java.text.DecimalFormat("0.0");
                    text_seller_credit.setText(format2.format(b.getFloat("credit")));
                    if (Objects.requireNonNull(b.getString("gender")).equals("female")) {
                        icon_gender.setImageResource(R.drawable.ic_woman);
                    }
                    String lastLoginDate = "最晚" + b.getString("lastLoginDate") + "来过";
                    text_login_date.setText(lastLoginDate);
                    RequestOptions options2 = new RequestOptions()
                            .placeholder(R.drawable.ic_no_image) // 图片加载出来前，显示的图片
                            .fallback(R.drawable.ic_home) // url为空的时候,显示的图片
                            .error(R.drawable.ic_fail); // 图片加载失败后，显示的图片
                    Glide.with(getApplicationContext()).load(b.getByteArray("studentImage")).apply(options2).into(image_seller);
                }
                Log.i("Handler", "Query Products Info");
            }
        };
        // 添加收藏反馈
        addFavoriteHandler = new Handler() {
            public void handleMessage (Message msg){
                Bundle b = msg.getData();
                if (b.getInt("errorCode") != 0) {
                    NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(ProductInfoActivity.this);
                    dialogBuilder
                            .withTitle("添加收藏失败")
                            .withMessage(b.getString("e"))
                            .withDialogColor("#770000")
                            .withIcon(getResources().getDrawable(R.drawable.ic_fail))
                            .withEffect(Effectstype.SlideBottom)
                            .show();
                    icon_favorite.setImageResource(R.drawable.ic_star_white);
                    icon_favorite.setTag("unfavor");
                }
            }
        };
        // 删除收藏反馈
        cancelFavoriteHandler = new Handler() {
            public void handleMessage (Message msg){
                Bundle b = msg.getData();
                if (b.getInt("errorCode") != 0) {
                    NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(ProductInfoActivity.this);
                    dialogBuilder
                            .withTitle("取消收藏失败")
                            .withMessage(b.getString("e"))
                            .withDialogColor("#770000")
                            .withIcon(getResources().getDrawable(R.drawable.ic_fail))
                            .withEffect(Effectstype.SlideBottom)
                            .show();
                    icon_favorite.setImageResource(R.drawable.ic_star);
                    icon_favorite.setTag("favor");
                }
            }
        };
        // 根据是否收藏来设定图标
        isFavoriteHandler = new Handler() {
            public void handleMessage (Message msg){
                Bundle b = msg.getData();
                if (b.getInt("errorCode") == 0) {
                    if (b.getBoolean("favorite")) {
                        icon_favorite.setImageResource(R.drawable.ic_star);
                        icon_favorite.setTag("favor");
                    } else {
                        icon_favorite.setImageResource(R.drawable.ic_star_white);
                        icon_favorite.setTag("unfavor");
                    }
                }
            }
        };
        // 是否留言成功 & 刷新界面
        commentHandler = new Handler() {
            public void handleMessage (Message msg){
                Bundle b = msg.getData();
                if (b.getInt("errorCode") == 0) {
                    Student student = Student.getCurrentUser(Student.class);
                    if (b.getString("commentFatherId") == null) {
                        if (student.getImage() != null) {
                            commentList.add(new CommentView(b.getString("commentId"),
                                    student.getObjectId(),
                                    student.getName(),
                                    Bytes.toArray(student.getImage()),
                                    commentContent,
                                    b.getString("date"), null));
                        } else {
                            commentList.add(new CommentView(b.getString("commentId"),
                                    student.getObjectId(),
                                    student.getName(), null,
                                    commentContent,
                                    b.getString("date"), null));
                        }
                        expandableAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), b.getString("e"), Toast.LENGTH_LONG).show();
                }
            }
        };
        // 获取所有留言
        productCommentHandler = new Handler() {
            public void handleMessage (Message msg){
                Bundle bs = msg.getData();
                if (bs.getInt("errorCode") == 0) {
                    bs.remove("errorCode");
                    for (int i = 0; !bs.isEmpty(); ++i) {
                        Bundle b = bs.getBundle(String.valueOf(i));
                        assert b != null;
                        if (b.getString("commenterFatherId") == null) {
                            CommentView comment = new CommentView(
                                    b.getString("commentId"),
                                    b.getString("commenterId"),
                                    b.getString("commenterName"),
                                    b.getByteArray("image"),
                                    b.getString("content"),
                                    b.getString("date"), null);
                            commentList.add(comment);
                            CommentRepository.getInstance(new CommentDataSource()).queryReplies(comment.getCommentId(), commentList.size() - 1);
                        }
                        bs.remove(String.valueOf(i));
                    }
                    Log.i("Handler", "Query All Comments By Product");
                    expandableAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), bs.getString("e"), Toast.LENGTH_LONG).show();
                }
            }
        };
        // reply
        replyHandler = new Handler() {
            public void handleMessage (Message msg){
                Bundle bs = msg.getData();
                int position = bs.getInt("position");
                List<CommentView> replyList = new ArrayList<>();
                if (bs.getInt("errorCode") == 0) {
                    bs.remove("errorCode");
                    bs.remove("position");
                    for (int i = 0; !bs.isEmpty(); ++i) {
                        Bundle b = bs.getBundle(String.valueOf(i));
                        assert b != null;
                        CommentView comment = new CommentView(
                                b.getString("commentId"),
                                b.getString("commenterId"),
                                b.getString("commenterName"),
                                b.getByteArray("image"),
                                b.getString("content"),
                                b.getString("date"), null);
                        replyList.add(comment);
                        bs.remove(String.valueOf(i));
                    }
                    Log.i("Handler", "Query All Reply By Comment");
                    CommentView comment = commentList.get(position);
                    comment.setReplyList(replyList);
                    commentList.set(position, comment);
                    expandableAdapter.notifyDataSetChanged();
                    commentExpandableListView.expandGroup(position);
                } else {
                    Toast.makeText(getApplicationContext(), bs.getString("e"), Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private void initView() {
        text_product_name = findViewById(R.id.text_product_name);
        text_price = findViewById(R.id.text_price);
        text_old_price = findViewById(R.id.text_old_price);
        text_product_description = findViewById(R.id.text_description);
        text_seller_name = findViewById(R.id.text_seller_name);
        text_seller_credit = findViewById(R.id.text_seller_credit);
        text_login_date = findViewById(R.id.text_login_date);
        text_publish_date = findViewById(R.id.text_publish_date);
        image_product = findViewById(R.id.image_product);
        image_seller = findViewById(R.id.image_seller);
        icon_favorite = findViewById(R.id.icon_favorite);
        icon_gender = findViewById(R.id.icon_gender);
        layout_seller = findViewById(R.id.layout_seller);
        text_new = findViewById(R.id.text_new);
        text_cannot_bargain = findViewById(R.id.text_cannot_bargain);
        commentExpandableListView = findViewById(R.id.comment_list_view);
    }

    /**
     * 点击“收藏”
     */
    private void clickFavorite() {
        if (icon_favorite.getTag().equals("favor")) {
            Log.i("ProductInfo", "Remove Favorite");
            icon_favorite.setImageResource(R.drawable.ic_star_white);
            icon_favorite.setTag("unfavor");
            FavoriteRepository.getInstance(new FavoriteDataSource()).removeFavorite(productId);
        } else {
            Log.i("ProductInfo", "Add Favorite");
            icon_favorite.setImageResource(R.drawable.ic_star);
            icon_favorite.setTag("favor");
            FavoriteRepository.getInstance(new FavoriteDataSource()).saveFavorite(productId);
        }
    }

    /**
     * 点击联系卖家
     */
    private void chat() {
        Intent intent = new Intent(getApplicationContext(), ConversationActivity.class);
        intent.putExtra("product_id_extra", productId);
        intent.putExtra("chat_id_extra", sellerId);
        intent.putExtra("seller_id_extra", sellerId);
        intent.putExtra("seller_name_extra", sellerName);
        startActivity(intent);
    }


    /**
     * 评论
     */

    // 弹出评论框
    private void comment() {
        bottomSheetDialog = new BottomSheetDialog(this,R.style.BottomSheetEdit);
        View commentView = LayoutInflater.from(this).inflate(R.layout.item_comment_dialog,null);
        final EditText commentText = commentView.findViewById(R.id.comment_edittext);
        final Button bt_comment = commentView.findViewById(R.id.comment_launch);
        bottomSheetDialog.setContentView(commentView);

        bt_comment.setOnClickListener(view -> {
            commentContent = commentText.getText().toString().trim();
            if(!TextUtils.isEmpty(commentContent)){
                //commentOnWork(commentContent);
                bottomSheetDialog.dismiss();
                CommentRepository.getInstance(new CommentDataSource()).saveComment(productId, commentContent, null);

                Toast.makeText(ProductInfoActivity.this,"评论成功",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(ProductInfoActivity.this,"评论内容不能为空",Toast.LENGTH_SHORT).show();
            }
        });
        bottomSheetDialog.show();
    }

    /**
     * func:弹出回复框
     */
    private void showReplyDialog(final int position){
        bottomSheetDialog = new BottomSheetDialog(this,R.style.BottomSheetEdit);
        View commentView = LayoutInflater.from(this).inflate(R.layout.item_comment_dialog,null);
        final EditText commentText = commentView.findViewById(R.id.comment_edittext);
        final Button bt_comment = commentView.findViewById(R.id.comment_launch);

        commentText.setHint("回复 " + commentList.get(position).getCommenterName() + " 的评论:");
        bottomSheetDialog.setContentView(commentView);
        bt_comment.setOnClickListener(view -> {
            commentContent = commentText.getText().toString().trim();
            if(!TextUtils.isEmpty(commentContent)){
                bottomSheetDialog.dismiss();

                CommentRepository.getInstance(new CommentDataSource()).saveComment(productId, commentContent, commentList.get(position).getCommentId());
                commentExpandableListView.expandGroup(position);
                Toast.makeText(ProductInfoActivity.this,"回复成功",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(ProductInfoActivity.this,"回复内容不能为空",Toast.LENGTH_SHORT).show();
            }
        });
        bottomSheetDialog.show();
    }

    /**
     * 初始化评论和回复列表
     */
    private void initExpandableListView(){
        commentExpandableListView.setGroupIndicator(null);
        //默认展开所有回复
        expandableAdapter = new ExpandableAdapter(this, commentList);
        commentExpandableListView.setAdapter(expandableAdapter);
        for(int i = 0; i < commentList.size(); i++){
            commentExpandableListView.expandGroup(i);
        }
        commentExpandableListView.setOnGroupClickListener((expandableListView, view, groupPosition, l) -> {
            boolean isExpanded = expandableListView.isGroupExpanded(groupPosition);
            showReplyDialog(groupPosition);
            return true;
        });

        commentExpandableListView.setOnChildClickListener((expandableListView, view, groupPosition, childPosition, l) -> {
            Toast.makeText(ProductInfoActivity.this,"点击了回复",Toast.LENGTH_SHORT).show();
            return false;
        });
    }
}


