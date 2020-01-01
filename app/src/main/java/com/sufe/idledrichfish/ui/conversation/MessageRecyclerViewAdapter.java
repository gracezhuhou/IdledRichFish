package com.sufe.idledrichfish.ui.conversation;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sufe.idledrichfish.R;

import java.util.List;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.MyViewHolder> {

    private List<MessageView> mMsgList;

    public MessageRecyclerViewAdapter(List<MessageView> mMsgList) {
        this.mMsgList = mMsgList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_chat_msg, null);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MessageView msg = mMsgList.get(position);
        if (msg.getType() == MessageView.TYPE_RECEIVED) {
            //如果是收到的消息，显示左边布局，隐藏右边布局
            holder.layout_left.setVisibility(View.VISIBLE);
            holder.layout_right.setVisibility(View.GONE);
            holder.text_left.setText(msg.getContent());
        } else if (msg.getType() == MessageView.TYPE_SEND) {
            //如果是发送的消息，显示右边布局，隐藏左边布局
            holder.layout_left.setVisibility(View.GONE);
            holder.layout_right.setVisibility(View.VISIBLE);
            holder.text_right.setText(msg.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout_left;
        LinearLayout layout_right;
        TextView text_left;
        TextView text_right;

        public MyViewHolder(View itemView) {
            super(itemView);
            layout_left = itemView.findViewById(R.id.ll_msg_left);
            layout_right = itemView.findViewById(R.id.ll_msg_right);
            text_left = itemView.findViewById(R.id.tv_msg_left);
            text_right = itemView.findViewById(R.id.tv_msg_right);
        }
    }
}
