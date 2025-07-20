package com.example.nikeshop.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.nikeshop.data.local.AppDatabase;
import com.example.nikeshop.data.local.dao.OrderDao;
import com.example.nikeshop.data.local.entity.Order;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderRepository {
    private final OrderDao orderDao;
    private final ExecutorService executorService;

    public OrderRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        orderDao = db.orderDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Order>> getAllOrders() {
        return orderDao.getAllOrders();
    }

    public LiveData<List<Order>> search(String keyword) {
        return orderDao.searchOrders(keyword);
    }

    public void insert(Order order) {
        executorService.execute(() -> orderDao.insert(order));
    }

    public void update(Order order) {
        executorService.execute(() -> orderDao.update(order));
    }

    public void delete(Order order) {
        executorService.execute(() -> orderDao.delete(order));
    }

    public LiveData<Order> getOrderById(int id) {
        return orderDao.getOrderById(id);
    }

    public LiveData<List<Order>> getOrdersByStatus(String status) {
        return orderDao.getOrdersByStatus(status);
    }


}
