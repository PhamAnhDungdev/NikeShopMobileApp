package com.example.nikeshop.ui.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.nikeshop.NikeShopApp;
import com.example.nikeshop.data.local.AppDatabase;
import com.example.nikeshop.data.local.entity.Order;
import com.example.nikeshop.data.repositories.OrderRepository;

import java.util.List;

public class OrderViewModel extends AndroidViewModel {

    private final OrderRepository orderRepository;
    private final LiveData<List<Order>> allOrders;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = NikeShopApp.getDatabase();
        orderRepository = new OrderRepository(db.orderDao());
        allOrders = orderRepository.getAllOrders();
    }

    public LiveData<List<Order>> getAllOrders() {
        return allOrders;
    }

    public LiveData<List<Order>> getOrdersByUser(int userId) {
        return orderRepository.getOrdersByUser(userId);
    }

    public LiveData<List<Order>> getOrdersByStatus(String status) {
        return orderRepository.getOrdersByStatus(status);
    }

    public void insertOrder(Order order) {
        orderRepository.insertOrder(order);
    }

    public void deleteOrder(Order order) {
        orderRepository.deleteOrder(order);
    }

    public void deleteAllOrders() {
        orderRepository.deleteAllOrders();
    }
}
