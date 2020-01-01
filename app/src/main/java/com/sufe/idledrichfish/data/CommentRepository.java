package com.sufe.idledrichfish.data;

public class CommentRepository {
    private static volatile CommentRepository instance;

    private CommentDataSource dataSource;

    // private constructor : singleton access
    private CommentRepository(CommentDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static CommentRepository getInstance(CommentDataSource dataSource) {
        if (instance == null) {
            instance = new CommentRepository(dataSource);
        }
        return instance;
    }

    // 添加留言
    public void saveComment(String productId, String content, String commentFatherId) {
        dataSource.saveComment(productId, content, commentFatherId);
    }

    // 根据productId获取留言
    public void queryCommentsByProduct(String productId) {
        dataSource.queryCommentsByProduct(productId);
    }

    public void queryReplies(String commentFatherId, int position) {
        dataSource.queryReplies(commentFatherId, position);
    }
}
