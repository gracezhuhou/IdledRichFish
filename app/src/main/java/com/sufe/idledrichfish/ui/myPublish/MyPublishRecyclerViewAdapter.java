package com.sufe.idledrichfish.ui.myPublish;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.data.model.Product;

import java.text.DecimalFormat;
import java.util.List;

public class MyPublishRecyclerViewAdapter extends RecyclerView.Adapter<MyPublishRecyclerViewAdapter.ViewHolder>{
    private List<Product> myProducts;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private SwipeLayout swipe_layout;
        private TextView text_product_name;
        private TextView text_price;
        private ImageView image_product1;
        private ImageView image_product2;
        private ImageView image_product3;
        private ImageView image_product4;
        private Button button_delete;
        private String productId;

        public ViewHolder(View view){
            super(view);
            swipe_layout = view.findViewById(R.id.swipe_layout);
            text_product_name = view.findViewById(R.id.text_product_name);
            text_price = view.findViewById(R.id.text_price);
            button_delete = view.findViewById(R.id.button_delete);
        }
    }

    public MyPublishRecyclerViewAdapter(List<Product> products){
        myProducts = products;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_publish_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        int myPosition = holder.getAdapterPosition();
        // 设置商品信息
        Product product = myProducts.get(myPosition);
        holder.text_product_name.setText(product.getName());
        DecimalFormat format = new java.text.DecimalFormat("¥ 0.00"); // 保留小数点两位
        holder.text_price.setText(format.format(product.getPrice()));
        holder.productId = product.getObjectId();
        // 设置商品图片
//        if (!product.getImage1().getUrl().equals("")) {
//            holder.image_product.setImageDrawable(LoadImageFromUrl(product.getImage1().getUrl()));
//        }


        holder.swipe_layout.setShowMode(SwipeLayout.ShowMode.LayDown);
        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myProducts.remove(myPosition);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount(){
        return myProducts.size();
    }

}
