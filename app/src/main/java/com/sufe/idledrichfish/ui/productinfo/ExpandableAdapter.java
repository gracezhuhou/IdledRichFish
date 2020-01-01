package com.sufe.idledrichfish.ui.productinfo;

import android.content.Context;
import android.graphics.Color;
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
import com.sufe.idledrichfish.data.model.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExpandableAdapter extends BaseExpandableListAdapter {

    private List<Comment> commentList;
    private Context context;
    private int pageIndex = 1;
    private CommentRepository commentRepository = CommentRepository.getInstance(new CommentDataSource());
    static public Handler groupCommentHandler;

    public ExpandableAdapter(Context context, List<Comment> commentBeanList) {
        this.context = context;
        this.commentList = commentBeanList;
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

        commentRepository.queryReplies();

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
        Glide.with(context).load(commentList.get(groupPosition).getCommenter().getImage()).apply(options).into(groupHolder.g_imageView);
        groupHolder.g_name.setText(commentList.get(groupPosition).getCommenter().getName());
        groupHolder.g_time.setText(commentList.get(groupPosition).getDate().getDate());
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
        Glide.with(context).load(commentList.get(groupPosition).getCommenter().getImage()).apply(options).into(childHolder.c_imageView);
        childHolder.c_name.setText(commentList.get(groupPosition).getReplyList().get(childPosition).getCommenter().getName());
        childHolder.c_father.setText(commentList.get(groupPosition).getReplyList().get(childPosition).getCommentFather().getName() + ":");
        childHolder.c_date.setText(commentList.get(groupPosition).getDate().getDate());
        childHolder.c_content.setText(commentList.get(groupPosition).getReplyList().get(childPosition).getContent());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     *  新的评论数据
     */
    public void addTheCommentData(Comment comment){
        if(comment!=null){

            commentList.add(comment);
            notifyDataSetChanged();
        }else {
            throw new IllegalArgumentException("评论数据为空!");
        }

    }

    /**
     * 新的回复数据
     */
    public void addTheReplyData(Comment reply, int groupPosition){
        if(reply!=null){
            Log.e("reply", "addTheReplyData: >>>>该刷新回复列表了:"+reply.toString() );
            if(commentList.get(groupPosition).getReplyList() != null ){
                commentList.get(groupPosition).getReplyList().add(reply);
            }else {
                List<Comment> replyList = new ArrayList<>();
                replyList.add(reply);
                commentList.get(groupPosition).setReplyList(replyList);
            }
            notifyDataSetChanged();
        }else {
            throw new IllegalArgumentException("回复数据为空!");
        }

    }

    /**
     * func:添加和展示所有回复
     * replyBeanList 所有回复数据
     * groupPosition 当前的评论
     */
    private void addReplyList(List<Comment> replyList, int groupPosition){
        if(commentList.get(groupPosition).getReplyList() != null ){
            commentList.get(groupPosition).getReplyList().clear();
            commentList.get(groupPosition).getReplyList().addAll(replyList);
        }else {

            commentList.get(groupPosition).setReplyList(replyList);
        }

        notifyDataSetChanged();
    }
}
