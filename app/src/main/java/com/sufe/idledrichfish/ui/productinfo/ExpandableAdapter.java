package com.sufe.idledrichfish.ui.productinfo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.data.CommentDataSource;
import com.sufe.idledrichfish.data.CommentRepository;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExpandableAdapter extends BaseExpandableListAdapter {

    private List<CommentView> commentList = new ArrayList<>();
    private Context context;
    private int pageIndex = 1;
    private CommentRepository commentRepository = CommentRepository.getInstance(new CommentDataSource());

    public ExpandableAdapter(Context context, List<CommentView> commentViewList) {
        this.context = context;
        this.commentList = commentViewList;
    }

    @Override
    public int getGroupCount() {
        return commentList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(commentList.get(groupPosition).getReplyList() == null){
            return 0;
        }else {
            return commentList.get(groupPosition).getReplyList().size()>0 ? commentList.get(groupPosition).getReplyList().size():0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return commentList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return commentList.get(groupPosition).getReplyList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getCombinedChildId(groupPosition, childPosition);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    boolean isLike = false;

    private class GroupHolder{
        private CircleImageView g_imageView;
        private TextView g_name, g_content, g_time;
        private ImageView iv_like;
        private List<CommentView> replyList = new ArrayList<>();

        public GroupHolder(View view) {
            g_imageView = (CircleImageView) view.findViewById(R.id.image);
            g_name = (TextView) view.findViewById(R.id.name);
            g_content = (TextView) view.findViewById(R.id.content);
            g_time = (TextView) view.findViewById(R.id.date);
            iv_like = (ImageView) view.findViewById(R.id.comment_item_like);
        }

    }

    private class ChildHolder{
        private CircleImageView c_imageView;
        private TextView c_name, c_content, c_date, c_father;
        public ChildHolder(View view) {
            c_imageView = (CircleImageView) view.findViewById(R.id.image);
            c_name = (TextView) view.findViewById(R.id.name);
            c_father = (TextView) view.findViewById(R.id.father);
            c_content = (TextView) view.findViewById(R.id.content);
            c_date = (TextView) view.findViewById(R.id.date);
        }
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupHolder groupHolder;

        CommentView comment = commentList.get(groupPosition);

        commentRepository.queryReplies(comment.getCommentId(), groupPosition);

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false);
            groupHolder = new GroupHolder(convertView);
            convertView.setTag(groupHolder);
        }else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_no_image) // 图片加载出来前，显示的图片
                .fallback(R.drawable.head) // url为空的时候,显示的图片
                .error(R.drawable.ic_fail); // 图片加载失败后，显示的图片
        Glide.with(context).load(commentList.get(groupPosition).getImage()).apply(options).into(groupHolder.g_imageView);
        groupHolder.g_name.setText(commentList.get(groupPosition).getCommenterName());
        groupHolder.g_time.setText(commentList.get(groupPosition).getDate());
        groupHolder.g_content.setText(commentList.get(groupPosition).getContent());
        groupHolder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLike){
                    isLike = false;
                    groupHolder.iv_like.setColorFilter(Color.parseColor("#aaaaaa"));
                }else {
                    isLike = true;
                    groupHolder.iv_like.setColorFilter(Color.parseColor("#FF5C5C"));
                }
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildHolder childHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_child,parent, false);
            childHolder = new ChildHolder(convertView);
            convertView.setTag(childHolder);
        }
        else {
            childHolder = (ChildHolder) convertView.getTag();
        }

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_no_image) // 图片加载出来前，显示的图片
                .fallback(R.drawable.head) // url为空的时候,显示的图片
                .error(R.drawable.ic_fail); // 图片加载失败后，显示的图片
        Glide.with(context).load(commentList.get(groupPosition).getImage()).apply(options).into(childHolder.c_imageView);
        String name = commentList.get(groupPosition).getReplyList().get(childPosition).getCommenterName();
        childHolder.c_name.setText(name);
        childHolder.c_father.setText(name + ":");
        childHolder.c_date.setText(commentList.get(groupPosition).getDate());
        childHolder.c_content.setText(commentList.get(groupPosition).getReplyList().get(childPosition).getContent());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
