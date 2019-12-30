package com.sufe.idledrichfish.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.SpinKitView;
import com.sufe.idledrichfish.ProductInfoActivity;
import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.data.StudentDataSource;
import com.sufe.idledrichfish.data.StudentRepository;
import com.sufe.idledrichfish.data.model.Product;
import com.sufe.idledrichfish.data.model.Student;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>{
    private List<HomeProductView> myProducts;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private ConstraintLayout layout_product;
        private TextView text_product_name;
        private TextView text_product_price;
        private TextView text_seller_name;
        private TextView text_seller_credit;
        private ImageView image_product;
        private CircleImageView image_seller;
        private Context context;
        private String productId;

        public ViewHolder(View view){
            super(view);
            context = view.getContext();
            layout_product = view.findViewById(R.id.layout_product);
            text_product_name = view.findViewById(R.id.text_product_name);
            text_product_price = view.findViewById(R.id.text_product_price);
            text_seller_name = view.findViewById(R.id.text_seller_name);
            text_seller_credit = view.findViewById(R.id.text_seller_credit);
            image_product = view.findViewById(R.id.image_product);
            image_seller = view.findViewById(R.id.image_seller);
        }
    }

    public HomeRecyclerViewAdapter(List<HomeProductView> products){
        myProducts = products;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        // 设置item宽度 —— 1/2屏幕
        DisplayMetrics dm = holder.context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        holder.image_product.setMinimumWidth((width - 21) / 2);
        holder.image_product.setMaxWidth((width - 21) / 2);

        // 显示商品信息
        HomeProductView product = myProducts.get(holder.getAdapterPosition());
        holder.text_product_name.setText(product.getName());
        DecimalFormat format = new java.text.DecimalFormat("¥ 0.00"); // 保留小数点两位
        holder.text_product_price.setText(format.format(product.getPrice()));
        holder.productId = product.getObjectId();
        // 设置商品图片
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_no_image) // 图片加载出来前，显示的图片
                .fallback(R.drawable.ic_no_image) // url为空的时候,显示的图片
                .error(R.drawable.ic_fail); // 图片加载失败后，显示的图片
        Glide.with(holder.itemView).load(product.getImage1()).apply(options).into(holder.image_product);

        // 显示卖家信息
        holder.text_seller_name.setText(product.getSellerName());
        holder.text_seller_credit.setText(String.valueOf(product.getCredit()));
        RequestOptions options2 = new RequestOptions()
                .placeholder(R.drawable.ic_no_image) // 图片加载出来前，显示的图片
                .fallback(R.drawable.ic_home) // url为空的时候,显示的图片
                .error(R.drawable.ic_fail); // 图片加载失败后，显示的图片
        Glide.with(holder.context).load(product.getSellerImage()).apply(options2).into(holder.image_seller);


        // 点击跳转至商品详细界面
        holder.layout_product.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ProductInfoActivity.class);
            intent.putExtra("product_id_extra", holder.productId);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount(){
        return myProducts.size();
    }
}
