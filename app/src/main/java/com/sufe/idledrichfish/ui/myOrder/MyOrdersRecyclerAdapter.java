package com.sufe.idledrichfish.ui.myOrder;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sufe.idledrichfish.R;

import java.text.DecimalFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyOrdersRecyclerAdapter extends RecyclerView.Adapter<MyOrdersRecyclerAdapter.ViewHolder>{
    private List<MyOrderView> myOrders;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView text_order_id;
        private TextView text_status;
        private TextView text_product_name;
        private TextView text_price;
        private TextView text_seller_name;
        private ImageView image_product;
        private CircleImageView image_seller;
//        private Context context;

        public ViewHolder(View view){
            super(view);
//            context = view.getContext();
            text_order_id = view.findViewById(R.id.text_order_id);
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
        holder.text_order_id.setText(order.getOrderId());
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

//        holder.swipe_layout.setShowMode(SwipeLayout.ShowMode.LayDown);
//
//        // 点击删除发布商品
//        holder.button_delete.setOnClickListener(view -> {
//            ProductRepository.getInstance(new ProductDataSource()).deleteProduct(holder.productId);
//        });
//
//        // 点击商品跳转至商品详情页ProductInfoActivity
//        holder.layout_my_publish.setOnClickListener(view -> {
//            Intent intent = new Intent(view.getContext(), ProductInfoActivity.class);
//            intent.putExtra("product_id_extra", holder.productId);
//            view.getContext().startActivity(intent);
//        });
//
//        // 获取Bmob信息：删除发布商品是否成功
//        deleteHandler = new Handler() {
//            public void handleMessage(Message msg) {
//                int errorCode = msg.getData().getInt("errorCode");
//                Log.i("Handler", "Delete My Publish, Error Code " + errorCode);
//
//                if (errorCode == 0) {
//                    Snackbar snackbar = Snackbar.make(holder.button_delete, "商品删除成功", Snackbar.LENGTH_SHORT);
//                    snackbar.setActionTextColor(Color.WHITE);
//                    View snackbarView = snackbar.getView(); // 获取Snackbar显示的View对象
//                    snackbarView.setBackgroundColor(holder.context.getResources().getColor(R.color.orange));
//                    snackbar.show();
//                    // 删除RecyclerView中的该商品
//                    myProducts.remove(holder.getAdapterPosition());
//                    notifyDataSetChanged();
//                } else {
//                    String fail = "商品删除失败，原因：" + errorCode + msg.getData().getString("e");
//                    Snackbar snackbar = Snackbar.make(holder.button_delete, fail, Snackbar.LENGTH_LONG);
//                    snackbar.setActionTextColor(Color.WHITE);
//                    View snackbarView = snackbar.getView();
//                    snackbarView.setBackgroundColor(holder.context.getResources().getColor(R.color.orange));
//                    snackbar.show();
//                }
//            }
//        };
    }

    @Override
    public int getItemCount(){
        return myOrders.size();
    }
}
