package com.sufe.idledrichfish.ui.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.swipe.SwipeLayout;
import com.sufe.idledrichfish.R;

import java.util.List;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.ViewHolder>{
    private List<MessageView> messages;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private SwipeLayout swipe_layout;
        private TextView text_name;
        private TextView text_last_message;
        private TextView text_date;
        private ImageView image;
        private Button button_delete;

        public ViewHolder(View view){
            super(view);
            swipe_layout = view.findViewById(R.id.swipe_layout);
            text_name = view.findViewById(R.id.text_name);
            text_last_message = view.findViewById(R.id.text_last_message);
            text_date = view.findViewById(R.id.text_date);
            image = view.findViewById(R.id.image);
            button_delete = view.findViewById(R.id.button_delete);
        }
    }

    public MessageRecyclerViewAdapter(List<MessageView> messages){
        this.messages = messages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        // 聊天对象信息
        MessageView message = messages.get(holder.getAdapterPosition());
        holder.text_name.setText(message.getName());
        holder.text_last_message.setText(message.getLastMessage());
        holder.text_date.setText(message.getDate());
        // image
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_no_image) // 图片加载出来前，显示的图片
                .fallback(R.drawable.head) // url为空的时候,显示的图片
                .error(R.drawable.ic_fail); // 图片加载失败后，显示的图片
        Glide.with(holder.itemView).load(message.getImage()).apply(options).into(holder.image);

        // 滑动删除
        holder.swipe_layout.setShowMode(SwipeLayout.ShowMode.LayDown);
        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messages.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
                //todo
            }
        });
    }

    @Override
    public int getItemCount(){
        return messages.size();
    }

}
