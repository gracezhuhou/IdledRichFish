package com.sufe.idledrichfish.data;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.google.common.primitives.Bytes;
import com.sufe.idledrichfish.data.model.Comment;
import com.sufe.idledrichfish.data.model.Product;
import com.sufe.idledrichfish.data.model.Student;
import com.sufe.idledrichfish.ui.productinfo.ProductInfoActivity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class CommentDataSource {

    /**
     * 保存留言
     */
    void saveComment(String productId, String content, String commentFatherId) {
        Message msg = new Message();
        Bundle b = new Bundle();

        final Comment comment = new Comment();
        comment.setCommenter(Student.getCurrentUser(Student.class));
        Comment commentFather = new Comment();
        commentFather.setObjectId(commentFatherId);
        comment.setCommentFather(commentFather);
        Product product = new Product();
        product.setObjectId(productId);
        comment.setProduct(product);
        comment.setContent(content);
        comment.setDate(new BmobDate(Tool.getNetTime())); // 获取网络时间

        comment.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                // 反馈给ChatActivity
                if(e == null) {
                    b.putInt("errorCode", 0);
                    b.putString("orderId", objectId);
                    Log.i("BMOB", "Save Comment Success");
                }
                else {
                    b.putInt("errorCode", e.getErrorCode());
                    b.putString("e", e.toString());
                    Log.e("BMOB", "Save Comment Fail", e);
                }
                msg.setData(b);
                ProductInfoActivity.commentHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 根据productId获取留言
     */
    void queryCommentsByProduct(String productId){
        BmobQuery<Comment> query = new BmobQuery<>();
        query.include("commenter");
        Product product = new Product();
        product.setObjectId(productId);
        query.addWhereEqualTo("product", new BmobPointer(product));
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> object, BmobException e) {
                Message msg = new Message();
                Bundle bs = new Bundle();
                if(e == null){
                    bs.putInt("errorCode", 0);
                    int i = 0;
                    for (Comment comment: object) {
                        Bundle b = new Bundle();
                        Student student = comment.getCommenter();
                        b.putString("commenterId", student.getObjectId());
                        if (student.getImage() != null) {
                            b.putByteArray("image", Bytes.toArray(student.getImage()));
                        }
                        b.putString("content", comment.getContent());
                        b.putString("date", comment.getDate().getDate());
                        bs.putBundle(String.valueOf(i), b);
                        ++i;
                    }
                    Log.i("BMOB","Query Reply Success");
                }else{
                    bs.putInt("errorCode", e.getErrorCode());
                    bs.putString("e", e.toString());
                    Log.i("BMOB","Query Reply Fail: " + e.getMessage());
                }
                msg.setData(bs);
//                OrderInfoActivity.orderHandler.sendMessage(msg);
            }
        });
    }

    /**
     * 获取所有回复
     */
    void queryReplies(String commentFatherId) {
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        Comment commentFather = new Comment();
        commentFather.setObjectId(commentFatherId);
        query.addWhereEqualTo("commentFather", new BmobPointer(commentFather));
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> object, BmobException e) {
                Message msg = new Message();
                Bundle bs = new Bundle();
                if(e == null){
                    bs.putInt("errorCode", 0);
                    int i = 0;
                    for (Comment comment: object) {
                        Bundle b = new Bundle();
                        Student student = comment.getCommenter();
                        b.putString("commenterId", student.getObjectId());
                        if (student.getImage() != null) {
                            b.putByteArray("image", Bytes.toArray(student.getImage()));
                        }
                        b.putString("content", comment.getContent());
                        b.putString("date", comment.getDate().getDate());
                        bs.putBundle(String.valueOf(i), b);
                        ++i;
                    }
                    Log.i("BMOB","Query Reply Success");
                }else{
                    bs.putInt("errorCode", e.getErrorCode());
                    bs.putString("e", e.toString());
                    Log.i("BMOB","Query Reply Fail: " + e.getMessage());
                }
                msg.setData(bs);
//                OrderInfoActivity.orderHandler.sendMessage(msg);
            }
        });
    }
}
