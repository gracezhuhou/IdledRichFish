package com.sufe.idledrichfish.ui.myFavorite;

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
import com.sufe.idledrichfish.data.model.Student;

import java.text.DecimalFormat;
import java.util.List;

public class MyFavoriteRecyclerViewAdapter extends RecyclerView.Adapter<MyFavoriteRecyclerViewAdapter.ViewHolder>{
private List<Product> myProducts;

static class ViewHolder extends RecyclerView.ViewHolder{
    private SwipeLayout swipe_layout;
    private TextView text_product_name;
    private TextView text_price;
    private TextView text_seller_name;
    private TextView text_credit;
    private ImageView image_seller;
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
        text_seller_name = view.findViewById(R.id.text_seller_name);
        text_credit = view.findViewById(R.id.text_credit);
        image_seller = view.findViewById(R.id.image_seller);
        image_product1 = view.findViewById(R.id.image_product1);
        image_product2 = view.findViewById(R.id.image_product2);
        image_product3 = view.findViewById(R.id.image_product3);
        image_product4 = view.findViewById(R.id.image_product4);
        button_delete = view.findViewById(R.id.button_delete);
    }
}

    public MyFavoriteRecyclerViewAdapter(List<Product> products){
        myProducts = products;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_favorate_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        int myPosition = holder.getAdapterPosition();
        // 设置商品信息
        Product product = myProducts.get(myPosition);
        holder.text_product_name.setText(product.getName());
        DecimalFormat format1 = new java.text.DecimalFormat("¥ 0.00"); // 保留小数点两位
        holder.text_price.setText(format1.format(product.getPrice()));
        holder.productId = product.getObjectId();
        // todo image

        Student student = product.getSeller();
        holder.text_seller_name.setText(product.getSeller().getName());
        DecimalFormat format2 = new java.text.DecimalFormat("0.0");
        holder.text_credit.setText(format2.format(product.getSeller().getCredit()));
        // todo: image

        // 滑动删除
        holder.swipe_layout.setShowMode(SwipeLayout.ShowMode.LayDown);
        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myProducts.remove(myPosition);
                notifyDataSetChanged();//todo
            }
        });
    }

    @Override
    public int getItemCount(){
        return myProducts.size();
    }

}
