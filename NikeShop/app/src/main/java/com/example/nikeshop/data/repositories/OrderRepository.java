package com.example.nikeshop.data.repositories;

import androidx.lifecycle.LiveData;
import com.example.nikeshop.data.local.dao.OrderDao;
import com.example.nikeshop.data.local.entity.Order;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderRepository {
    private final OrderDao orderDao;
    private final ExecutorService executorService;

    public OrderRepository(OrderDao orderDao) {
        this.orderDao = orderDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Order>> getAllOrders() {
        return orderDao.getAllOrders();
    }

    public LiveData<List<Order>> getOrdersByUser(int userId) {
        return orderDao.getOrdersByUser(userId);
    }

    public LiveData<List<Order>> getOrdersByStatus(String status) {
        return orderDao.getOrdersByStatus(status);
    }

    // Synchronous method for ViewModel
    public Order getOrderById(int orderId) {
        return orderDao.getOrderById(orderId);
    }

    public void insertOrder(Order order) {
        executorService.execute(() -> orderDao.insertOrder(order));
    }

    public void deleteOrder(Order order) {
        executorService.execute(() -> orderDao.deleteOrder(order));
    }

    public void deleteAllOrders() {
        executorService.execute(orderDao::deleteAllOrders);
    }
}
