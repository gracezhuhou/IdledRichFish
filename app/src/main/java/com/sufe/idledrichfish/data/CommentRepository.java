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

    public void queryReplies(String commentFatherId) {
        dataSource.queryReplies(commentFatherId);
    }
}
