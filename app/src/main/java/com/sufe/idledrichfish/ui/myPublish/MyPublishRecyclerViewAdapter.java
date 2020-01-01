package com.sufe.idledrichfish.ui.myPublish;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.sufe.idledrichfish.ui.productinfo.ProductInfoActivity;
import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.SquareImageView;
import com.sufe.idledrichfish.data.ProductDataSource;
import com.sufe.idledrichfish.data.ProductRepository;

import java.text.DecimalFormat;
import java.util.List;

public class MyPublishRecyclerViewAdapter extends RecyclerView.Adapter<MyPublishRecyclerViewAdapter.ViewHolder>{
    private List<MyPublishProductView> myProducts;
    static public Handler deleteHandler;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private SwipeLayout swipe_layout;
        private TextView text_product_name;
        private TextView text_price;
        private SquareImageView image_product1;
        private SquareImageView image_product2;
        private SquareImageView image_product3;
        private SquareImageView image_product4;
        private Button button_delete;
        private ConstraintLayout layout_my_publish;
        private String productId;
        private Context context;

        public ViewHolder(View view){
            super(view);
            context = view.getContext();
            swipe_layout = view.findViewById(R.id.swipe_layout);
            text_product_name = view.findViewById(R.id.text_product_name);
            text_price = view.findViewById(R.id.text_price);
            button_delete = view.findViewById(R.id.button_delete);
            image_product1 = view.findViewById(R.id.image_product1);
            image_product2 = view.findViewById(R.id.image_product2);
            image_product3 = view.findViewById(R.id.image_product3);
            image_product4 = view.findViewById(R.id.image_product4);
            layout_my_publish = view.findViewById(R.id.layout_my_publish);
        }
    }

    public MyPublishRecyclerViewAdapter(List<MyPublishProductView> products){
        myProducts = products;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_publish_product, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        // 设置商品信息
        MyPublishProductView product = myProducts.get(holder.getAdapterPosition());
        holder.text_product_name.setText(product.getName());
        DecimalFormat format = new java.text.DecimalFormat("¥ 0.00"); // 保留小数点两位
        holder.text_price.setText(format.format(product.getPrice()));
        holder.productId = product.getObjectId();
        // 设置商品图片
        if (product.getImage1() != null) {
            Glide.with(holder.context).load(product.getImage1()).into(holder.image_product1);
            Glide.with(holder.context).load(product.getImage2()).into(holder.image_product2);
            Glide.with(holder.context).load(product.getImage3()).into(holder.image_product3);
            Glide.with(holder.context).load(product.getImage4()).into(holder.image_product4);
        }

        holder.swipe_layout.setShowMode(SwipeLayout.ShowMode.LayDown);

        // 点击删除发布商品
        holder.button_delete.setOnClickListener(view -> {
            ProductRepository.getInstance(new ProductDataSource()).deleteProduct(holder.productId);
        });

        // 点击商品跳转至商品详情页ProductInfoActivity
        holder.layout_my_publish.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ProductInfoActivity.class);
            intent.putExtra("product_id_extra", holder.productId);
            view.getContext().startActivity(intent);
        });

        // 获取Bmob信息：删除发布商品是否成功
        deleteHandler = new Handler() {
            public void handleMessage(Message msg) {
                int errorCode = msg.getData().getInt("errorCode");
                Log.i("Handler", "Delete My Publish, Error Code " + errorCode);

                if (errorCode == 0) {
                    Snackbar snackbar = Snackbar.make(holder.button_delete, "商品删除成功", Snackbar.LENGTH_SHORT);
                    snackbar.setActionTextColor(Color.WHITE);
                    View snackbarView = snackbar.getView(); // 获取Snackbar显示的View对象
                    snackbarView.setBackgroundColor(holder.context.getResources().getColor(R.color.orange));
                    snackbar.show();
                    // 删除RecyclerView中的该商品
                    myProducts.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();
                } else {
                    String fail = "商品删除失败，原因：" + errorCode + msg.getData().getString("e");
                    Snackbar snackbar = Snackbar.make(holder.button_delete, fail, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.WHITE);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(holder.context.getResources().getColor(R.color.orange));
                    snackbar.show();
                }
            }
        };
    }

    @Override
    public int getItemCount(){
        return myProducts.size();
    }
}
