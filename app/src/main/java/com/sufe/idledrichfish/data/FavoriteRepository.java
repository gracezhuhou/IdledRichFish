package com.sufe.idledrichfish.data;

public class FavoriteRepository {
    private static volatile FavoriteRepository instance;

    private FavoriteDataSource dataSource;

    // private constructor : singleton access
    private FavoriteRepository(FavoriteDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static FavoriteRepository getInstance(FavoriteDataSource dataSource) {
        if (instance == null) {
            instance = new FavoriteRepository(dataSource);
        }
        return instance;
    }

    // 添加收藏
    public void saveFavorite(String productId) {
        dataSource.saveFavorite(productId);
    }

    // 删除收藏
    public void removeFavorite(String productId) {
        dataSource.removeFavorite(productId);
    }

    // 当前学生是否收藏
    public void isFavorite(String productId) {
        dataSource.isFavorite(productId);
    }

    // 获取当前学生所有收藏
    public void queryMyFavorite() {
        dataSource.queryMyFavorite();
    }
}
