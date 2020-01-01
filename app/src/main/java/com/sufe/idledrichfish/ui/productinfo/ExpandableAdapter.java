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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.common.primitives.Bytes;
import com.sufe.idledrichfish.R;
import com.sufe.idledrichfish.data.model.Comment;
import com.sufe.idledrichfish.data.model.Student;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExpandableAdapter extends BaseExpandableListAdapter {

    private List<CommentView> commentList;
    private Context context;

    static public Handler commentHandler;



    public ExpandableAdapter(Context context, List<CommentView> commentViewList) {
        this.context = context;
        this.commentList = commentViewList;
    }

    /*private void setHandler() {
        // 是否留言成功 & 刷新界面
        commentHandler = new Handler() {
            public void handleMessage (Message msg){
                Bundle b = msg.getData();
                if (b.getInt("errorCode") == 0) {
                    Student student = Student.getCurrentUser(Student.class);
                    commentList.add(new CommentView(b.getString("commentId"),
                            student.getObjectId(),
                            student.getName(),
                            Bytes.toArray(student.getImage()),
                            commentContent,
                            b.getString("date")
                    ));
                    expandableAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), b.getString("e"), Toast.LENGTH_LONG).show();
                }
            }
        };
    }*/

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
        childHolder.c_name.setText(commentList.get(groupPosition).getReplyList().get(childPosition).getCommenter().getName());
        childHolder.c_father.setText(commentList.get(groupPosition).getReplyList().get(childPosition).getCommentFather().getName() + ":");
        childHolder.c_date.setText(commentList.get(groupPosition).getDate());
        childHolder.c_content.setText(commentList.get(groupPosition).getReplyList().get(childPosition).getContent());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
