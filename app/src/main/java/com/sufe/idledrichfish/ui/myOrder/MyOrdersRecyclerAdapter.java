package com.sufe.idledrichfish.ui.myOrder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sufe.idledrichfish.OrderInfoActivity;
import com.sufe.idledrichfish.R;

import java.text.DecimalFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyOrdersRecyclerAdapter extends RecyclerView.Adapter<MyOrdersRecyclerAdapter.ViewHolder>{
    private List<MyOrderView> myOrders;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout layout_order;
        private TextView text_status;
        private TextView text_product_name;
        private TextView text_price;
        private TextView text_seller_name;
        private ImageView image_product;
        private CircleImageView image_seller;
        private String orderId;

        public ViewHolder(View view){
            super(view);
            layout_order = view.findViewById(R.id.layout_order);
            text_status = view.findViewById(R.id.text_status);
            text_product_name = view.findViewById(R.id.text_product_name);
            text_price = view.findViewById(R.id.text_price);
            text_seller_name = view.findViewById(R.id.text_seller_name);
            image_product = view.findViewById(R.id.image_product);
            image_seller = view.findViewById(R.id.image_seller);
        }
    }

    public MyOrdersRecyclerAdapter(List<MyOrderView> orders){
        myOrders = orders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_order, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        MyOrderView order = myOrders.get(holder.getAdapterPosition());
        // 订单信息
        holder.orderId = order.getOrderId();
        switch (order.getStatus()){
            case 0: holder.text_status.setText("订单进行中"); break;
            case 1: holder.text_status.setText("订单待评价"); break;
            case 2: holder.text_status.setText("订单已取消"); break;
            case 3: holder.text_status.setText("订单已完成"); break;
        }
        // 商品信息
        holder.text_product_name.setText(order.getProductName());
        DecimalFormat format = new java.text.DecimalFormat("¥ 0.00"); // 保留小数点两位
        holder.text_price.setText(format.format(order.getPrice()));
        // 商品图片
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_no_image) // 图片加载出来前，显示的图片
                .fallback(R.drawable.ic_no_image) // url为空的时候,显示的图片
                .error(R.drawable.ic_fail); // 图片加载失败后，显示的图片
        Glide.with(holder.itemView).load(order.getProductImage()).apply(options).into(holder.image_product);
        // 卖家信息
        holder.text_seller_name.setText(order.getSellerName());
        // 卖家头像
        Glide.with(holder.itemView).load(order.getSellerImage()).apply(options).into(holder.image_seller);

        // 点击跳转至订单详情页OrderInfoActivity
        holder.layout_order.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), OrderInfoActivity.class);
            intent.putExtra("order_id_extra", holder.orderId);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount(){
        return myOrders.size();
    }
}
