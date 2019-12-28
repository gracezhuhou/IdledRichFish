package com.sufe.idledrichfish.ui.chat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sufe.idledrichfish.R;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.MyViewHolder> {

    private List<Msg> mMsgList;

    public MsgAdapter(List<Msg> mMsgList) {
        this.mMsgList = mMsgList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.recyclerview_item, null);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Msg msg = mMsgList.get(position);
        if (msg.getType() == Msg.TYPE_RECEIVED) {
            //如果是收到的消息，显示左边布局，隐藏右边布局
            holder.llLeft.setVisibility(View.VISIBLE);
            holder.llRight.setVisibility(View.GONE);
            holder.tv_Left.setText(msg.getContent());
        } else if (msg.getType() == Msg.TYPE_SEND) {
            //如果是发送的消息，显示右边布局，隐藏左边布局
            holder.llLeft.setVisibility(View.GONE);
            holder.llRight.setVisibility(View.VISIBLE);
            holder.tv_Right.setText(msg.getContent());
        }
    }

    @Override
    public int getItemCount() {

        return mMsgList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout llLeft;
        RelativeLayout llRight;
        TextView tv_Left;
        TextView tv_Right;

        public MyViewHolder(View itemView) {
            super(itemView);
            llLeft = itemView.findViewById(R.id.ll_msg_left);
            llRight = itemView.findViewById(R.id.ll_msg_right);
            tv_Left = itemView.findViewById(R.id.tv_msg_left);
            tv_Right = itemView.findViewById(R.id.tv_msg_right);

        }
    }
}
