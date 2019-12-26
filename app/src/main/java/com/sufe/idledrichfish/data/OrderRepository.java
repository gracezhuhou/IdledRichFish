package com.sufe.idledrichfish.data;

public class OrderRepository {
    private static volatile OrderRepository instance;

    private OrderDataSource dataSource;

    // private constructor : singleton access
    private OrderRepository(OrderDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static OrderRepository getInstance(OrderDataSource dataSource) {
        if (instance == null) {
            instance = new OrderRepository(dataSource);
        }
        return instance;
    }

    // 添加订单
    public void saveOrder(String sellerId, String productId) {
        dataSource.saveOrder(sellerId, productId);
    }

    // 取消订单
    public void cancelOrder(String orderId) {
//        dataSource.cancelOrder(orderId);
    }

    // 查询订单信息
    public void queryOrder(String orderId) {
        dataSource.queryOrder(orderId);
    }

    // 获取当前学生所有订单
    public void queryMyOrder() {

    }
}
