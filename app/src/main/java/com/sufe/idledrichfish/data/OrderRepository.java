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
    public void saveOrder(String productId) {
        dataSource.saveOrder(productId);
    }

    // 更新订单状态
    public void updateOrderStatus(String orderId, int status) {
        dataSource.updateOrderStatus(orderId, status);
    }

    // 查询订单信息
    public void queryOrder(String orderId) {
        dataSource.queryOrder(orderId);
    }

    // 获取当前学生某状态所有订单
    public void queryOrders(int status) {
        dataSource.queryOrders(status);
    }
}
