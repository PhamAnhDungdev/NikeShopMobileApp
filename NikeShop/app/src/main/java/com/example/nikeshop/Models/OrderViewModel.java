package com.example.nikeshop.Models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.nikeshop.data.local.entity.Order;
import com.example.nikeshop.data.repositories.OrderRepository;

import java.util.List;

public class OrderViewModel extends AndroidViewModel {

    private final OrderRepository repository;
    private final LiveData<List<Order>> allOrders;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        repository = new OrderRepository(application);
        allOrders = repository.getAllOrders();
    }

    public LiveData<List<Order>> getAllOrders() {
        return allOrders;
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


    public LiveData<List<Order>> getOrdersByStatus(String status) {
        return repository.getOrdersByStatus(status);
    }




}
