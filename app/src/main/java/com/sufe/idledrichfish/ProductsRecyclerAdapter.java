package com.sufe.idledrichfish;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sufe.idledrichfish.database.helper.DbHelper;
import com.sufe.idledrichfish.database.Product;
import com.sufe.idledrichfish.database.Student;
import com.sufe.idledrichfish.database.helper.StudentDbHelper;

import java.util.List;

public class ProductsRecyclerAdapter extends RecyclerView.Adapter<ProductsRecyclerAdapter.ViewHolder>{
    private List<Product> myProducts;
    private StudentDbHelper studentDbHelper = new StudentDbHelper();


    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView text_product_title;
        private TextView text_product_price;
        private TextView text_product_id;
        private TextView text_seller_name;
        private TextView text_seller_credit;
        private ImageView image_product;
        private ImageView image_seller;
        private CardView cardView_product;

        public ViewHolder(View view){
            super(view);
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

    public ProductsRecyclerAdapter(List<Product> products){
        myProducts = products;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        Product product = myProducts.get(holder.getAdapterPosition());
        holder.text_product_title.setText(product.getName());
        holder.text_product_price.setText(String.valueOf(product.getPrice()));
        holder.text_product_id.setText(String.valueOf(product.getProductId()));

        Student seller = studentDbHelper.getStudentById(product.getPublisherId());
        holder.text_seller_name.setText(seller.getName());
        holder.text_seller_credit.setText(String.valueOf(seller.getCredit()));

        // 设置产品图片
        byte[] product_image = product.getImage();
        if (product_image != null) {
            Bitmap bitmap_product = BitmapFactory.decodeByteArray(product_image, 0, product_image.length);
            holder.image_product.setImageBitmap(bitmap_product);
        }

        // 设置卖家头像
        byte[] seller_image = seller.getImage();
        if (seller_image != null) {
            Bitmap bitmap_seller = BitmapFactory.decodeByteArray(seller_image, 0, seller_image.length);
            holder.image_seller.setImageBitmap(bitmap_seller);
        }

        // 点击跳转至商品详细界面
        holder.cardView_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                long product_id = Long.valueOf(holder.text_product_id.getText().toString());
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
