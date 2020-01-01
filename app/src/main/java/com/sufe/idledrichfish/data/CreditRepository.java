package com.sufe.idledrichfish.data;

public class CreditRepository {
    private static volatile CreditRepository instance;

    private CreditDataSource dataSource;

    // private constructor : singleton access
    private CreditRepository(CreditDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static CreditRepository getInstance(CreditDataSource dataSource) {
        if (instance == null) {
            instance = new CreditRepository(dataSource);
        }
        return instance;
    }

    // 添加订单
    public void saveOrder(int score, String sellerId, String orderId) {
        dataSource.saveCredit(score, sellerId, orderId);
    }
}
