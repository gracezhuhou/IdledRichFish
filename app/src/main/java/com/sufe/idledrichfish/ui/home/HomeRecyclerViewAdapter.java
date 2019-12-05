package com.sufe.idledrichfish.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import com.sufe.idledrichfish.data.ProductDataSource;
import com.sufe.idledrichfish.data.ProductRepository;
import com.sufe.idledrichfish.data.StudentDataSource;
import com.sufe.idledrichfish.data.StudentRepository;
import com.sufe.idledrichfish.data.model.Product;
import com.sufe.idledrichfish.data.model.Student;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>{
    private List<Product> myProducts;

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

    public HomeRecyclerViewAdapter(List<Product> products){
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

        // 设置商品信息
        Product product = myProducts.get(holder.getAdapterPosition());
        holder.text_product_title.setText(product.getName());
        holder.text_product_price.setText(String.valueOf(product.getPrice()));
        holder.text_product_id.setText(String.valueOf(product.getObjectId()));
        // 设置商品图片
        if (!product.getImage1().getUrl().equals("")) {
            holder.image_product.setImageDrawable(LoadImageFromUrl(product.getImage1().getUrl()));
        }

        StudentRepository.getInstance(new StudentDataSource()).queryStudentForHome(product.getSeller().getObjectId(), position);

        /*
         * 点击跳转至商品详细界面
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
    public void onBindViewHolder(final ViewHolder holder, final int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
//            Student seller = myProducts.get(holder.getAdapterPosition()).getSeller();
            Student seller = (Student) payloads.get(0);
            holder.text_seller_name.setText(seller.getName());
            holder.text_seller_credit.setText(String.valueOf(seller.getCredit()));
            // 设置卖家头像
            if (seller.getImage() != null && !seller.getImage().getUrl().equals("")) {
                holder.image_seller.setImageDrawable(LoadImageFromUrl(seller.getImage().getUrl()));
            }
        }
    }

    @Override
    public int getItemCount(){
        return myProducts.size();
    }

    /**
     * 显示URL图像
     * @param url String
     * @return Drawable
     */
    public static Drawable LoadImageFromUrl(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, "src name");
        } catch (Exception e) {
            return null;
        }
    }

}
