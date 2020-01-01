package com.sufe.idledrichfish.data.model;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobRelation;

public class Comment extends BmobObject {

    private Student commenter;

    private Product product;

    private String content;

    private BmobDate date;

    private Comment commentFather;

    private BmobRelation allReply;


    public Student getCommenter() {
        return commenter;
    }

    public void setCommenter(Student commenter) {
        this.commenter = commenter;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BmobDate getDate() {
        return date;
    }

    public void setDate(BmobDate date) {
        this.date = date;
    }

    public Comment getCommentFather() {
        return commentFather;
    }

    public void setCommentFather(Comment commentFather) {
        this.commentFather = commentFather;
    }

    public BmobRelation getAllReply() {
        return allReply;
    }

    public void setAllReply(BmobRelation allReply) {
        this.allReply = allReply;
    }
}
