package com.sufe.idledrichfish.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>{
    private List<HomeProductView> myProducts;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView text_product_name;
        private TextView text_product_price;
        private TextView text_seller_name;
        private TextView text_seller_credit;
        private ImageView image_product;
        private ImageView image_seller;
        private CardView card_product;
        private SpinKitView spin_kit;
        private Context context;
        private String productId;

        public ViewHolder(View view){
            super(view);
            context = view.getContext();
            text_product_name = view.findViewById(R.id.text_product_name);
            text_product_price = view.findViewById(R.id.text_product_price);
            text_seller_name = view.findViewById(R.id.text_seller_name);
            text_seller_credit = view.findViewById(R.id.text_seller_credit);
            image_product = view.findViewById(R.id.image_product);
            image_seller = view.findViewById(R.id.image_seller);
            card_product = view.findViewById(R.id.card_product);
            spin_kit = view.findViewById(R.id.spin_kit);
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
        holder.card_product.setMinimumWidth((width - 21) / 2);
        holder.image_product.setMinimumHeight((width - 21) / 2);
        holder.image_product.setMaxHeight((width - 21) / 2);
        holder.spin_kit.setMinimumWidth((width - 21) / 2);

        // 显示商品信息
        HomeProductView product = myProducts.get(holder.getAdapterPosition());
        holder.text_product_name.setText(product.getName());
        DecimalFormat format = new java.text.DecimalFormat("¥ 0.00"); // 保留小数点两位
        holder.text_product_price.setText(format.format(product.getPrice()));
        holder.productId = product.getObjectId();
        // 设置商品图片
        Glide.with(holder.context).load(product.getImage1()).into(holder.image_product);

        // 显示卖家信息
        holder.text_seller_name.setText(product.getSellerName());
        holder.text_seller_credit.setText(String.valueOf(product.getCredit()));
        Glide.with(holder.context).load(product.getSellerImage()).into(holder.image_product);


        // 点击跳转至商品详细界面
        holder.card_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), ProductInfoActivity.class);
                intent.putExtra("product_id_extra", holder.productId);
                v.getContext().startActivity(intent);
            }
        });
    }

//    @Override
//    public void onBindViewHolder(final ViewHolder holder, final int position, List<Object> payloads) {
//        if (payloads.isEmpty()) {
//            onBindViewHolder(holder, position);
//        } else {
////            Student seller = myProducts.get(holder.getAdapterPosition()).getSeller();
//            Student seller = (Student) payloads.get(0);
//            holder.text_seller_name.setText(seller.getName());
//            holder.text_seller_credit.setText(String.valueOf(seller.getCredit()));
//            // 设置卖家头像
//            if (seller.getImage() != null && !seller.getImage().getUrl().equals("")) {
////                holder.image_seller.setImageDrawable(LoadImageFromUrl(seller.getImage().getUrl()));
//            }
//        }
//    }

    @Override
    public int getItemCount(){
        return myProducts.size();
    }
}
