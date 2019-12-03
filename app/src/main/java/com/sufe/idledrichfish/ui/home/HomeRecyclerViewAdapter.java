package com.sufe.idledrichfish.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sufe.idledrichfish.ProductViewActivity;
import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.data.model.BmobProduct;
import com.sufe.idledrichfish.database.StudentBLL;

import java.util.List;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>{
    private List<HomeProductView> myProducts;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView text_product_title;
        private TextView text_product_price;
        private TextView text_product_id;
        private TextView text_seller_name;
        private TextView text_seller_credit;
        private ImageView image_product;
        private ImageView image_seller;
        private CardView cardView_product;
        private Context context;

        public ViewHolder(View view){
            super(view);
            context = view.getContext();
            text_product_title = view.findViewById(R.id.textView_productTitle);
            text_product_price = view.findViewById(R.id.textView_productPrice);
            text_product_id = view.findViewById(R.id.textView_productId);
            text_seller_name = view.findViewById(R.id.textView_sellerName);
            text_seller_credit = view.findViewById(R.id.textView_sellerCredit);
            text_seller_credit = view.findViewById(R.id.textView_sellerCredit);
            image_product = view.findViewById(R.id.imageView_product);
            image_seller = view.findViewById(R.id.imageView_seller);
            cardView_product = view.findViewById(R.id.cardView_main);
        }
    }

    public HomeRecyclerViewAdapter(List<HomeProductView> products){
        myProducts = products;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        // 设置item宽度 —— 1/2屏幕
        DisplayMetrics dm = holder.context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        holder.cardView_product.setMinimumWidth((width - 21) / 2);

        HomeProductView product = myProducts.get(holder.getAdapterPosition());
        holder.text_product_title.setText(product.getName());
        holder.text_product_price.setText(String.valueOf(product.getPrice()));
        holder.text_product_id.setText(String.valueOf(product.getObjectId()));
        holder.text_seller_name.setText(product.getSellerName());
        holder.text_seller_credit.setText(String.valueOf(product.getSellerCredit()));
        // 设置卖家头像
        if (product.getSellerImage() != null && !product.getSellerImage().equals("")) {
            Bitmap bitmap_seller = BitmapFactory.decodeFile(product.getSellerImage());
//            Bitmap bitmap_seller = BitmapFactory.decodeByteArray(seller_image, 0, seller_image.length);
            holder.image_seller.setImageBitmap(bitmap_seller);
        }
        // 设置商品图片
        if (product.getProductImage() != null && !product.getProductImage().equals("")) {
            Bitmap bitmap_product = BitmapFactory.decodeFile(product.getProductImage());
            holder.image_product.setImageBitmap(bitmap_product);
        }

        /*
        点击跳转至商品详细界面
         */
        holder.cardView_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String product_id = holder.text_product_id.getText().toString();
                Intent intent = new Intent(v.getContext(), ProductViewActivity.class);
                intent.putExtra("product_id_extra", product_id);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount(){
        return myProducts.size();
    }

}
