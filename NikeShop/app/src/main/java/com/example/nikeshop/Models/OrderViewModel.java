package com.example.nikeshop.Models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.nikeshop.data.local.AppDatabase;
import com.example.nikeshop.data.local.dao.CartDao;
import com.example.nikeshop.data.local.dao.OrderDao;
import com.example.nikeshop.data.local.dao.OrderDetailDao;
import com.example.nikeshop.data.local.dao.ProductDao;
import com.example.nikeshop.data.local.entity.Order;
import com.example.nikeshop.data.repositories.OrderRepository;

import java.util.List;

public class OrderViewModel extends AndroidViewModel {

    private final OrderRepository repository;
    private final LiveData<List<Order>> allOrders;
    private final LiveData<List<OrderDisplayItem>> allDisplayOrders;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        OrderDao orderDao = db.orderDao();
        OrderDetailDao orderDetailDao = db.orderDetailDao();
        CartDao cartDao = db.cartDao();
        ProductDao productDao = db.productDao();
        repository = new OrderRepository(orderDao, orderDetailDao, cartDao, productDao);
        allOrders = repository.getAllOrders();
        allDisplayOrders = repository.getAllOrderDisplays();
    }

    public LiveData<List<Order>> getAllOrders() {
        return allOrders;
    }

    public LiveData<List<Order>> getOrdersByStatus(String status) {
        return repository.getOrdersByStatus(status);
    }

    public LiveData<List<Order>> search(String keyword) {
        return repository.search(keyword);
    }

    public void insert(Order order) {
        repository.insert(order);
    }

    public void update(Order order) {
        repository.update(order);
    }

    public void delete(Order order) {
        repository.delete(order);
    }

    public LiveData<Order> getOrderById(int orderId) {
        return repository.getOrderById(orderId);
    }

    public LiveData<List<OrderDisplayItem>> getOrderDisplayItems() {
        return allDisplayOrders;
    }

    public LiveData<List<OrderDisplayItem>> getDisplayItemsByStatus(String status) {
        return repository.getDisplayByStatus(status);
    }

    public LiveData<List<OrderDisplayItem>> searchDisplayItems(String keyword) {
        return repository.searchDisplay(keyword);
    }
}
