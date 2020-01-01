package com.sufe.idledrichfish.ui.productinfo;

import java.util.List;

public class CommentView {

    private String commentId;

    private String commenterId;

    private String commenterName;

    private byte[] image;

    private String content;

    private String date;

    private String commenterFatherId;

    private List<CommentView> replyList;


    public CommentView(String commentId, String commenterId, String commenterName, byte[] image,
                       String content, String date, List<CommentView> replyList) {
        this.commentId = commentId;
        this.commenterId = commenterId;
        this.commenterName = commenterName;
        this.image = image;
        this.content = content;
        this.date = date;
        this.replyList = replyList;
    }


    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommenterId() {
        return commenterId;
    }

    public void setCommenterId(String commenterId) {
        this.commenterId = commenterId;
    }

    public String getCommenterName() {
        return commenterName;
    }

    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCommenterFatherId() {
        return commenterFatherId;
    }

    public void setCommenterFatherId(String commenterFatherId) {
        this.commenterFatherId = commenterFatherId;
    }

    public List<CommentView> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<CommentView> replyList) {
        this.replyList = replyList;
    }
}
