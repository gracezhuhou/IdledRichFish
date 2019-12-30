package com.sufe.idledrichfish.data;

import java.util.List;

public class TagRepository {
    private static volatile TagRepository instance;

    private TagDataSource dataSource;

    // private constructor : singleton access
    private TagRepository(TagDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static TagRepository getInstance(TagDataSource dataSource) {
        if (instance == null) {
            instance = new TagRepository(dataSource);
        }
        return instance;
    }

    // 添加tags
    public void saveTags(List<String> tags, String productId) {
        dataSource.saveTags(tags, productId);
    }

}

