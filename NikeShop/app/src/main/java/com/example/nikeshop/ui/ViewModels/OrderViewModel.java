package com.example.nikeshop.ui.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.nikeshop.NikeShopApp;
import com.example.nikeshop.data.local.AppDatabase;
import com.example.nikeshop.data.local.entity.Order;
import com.example.nikeshop.data.local.modelDto.ProductOrderDto;
import com.example.nikeshop.data.repositories.OrderRepository;

import java.util.List;
import java.util.function.Consumer;

public class OrderViewModel extends AndroidViewModel {

    private final OrderRepository orderRepository;
    private final LiveData<List<Order>> allOrders;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = NikeShopApp.getDatabase();

        orderRepository = new OrderRepository(db.orderDao(), db.orderDetailDao(), db.cartDao());
        allOrders = orderRepository.getAllOrders();
    }

    public void placeOrder(int userId, double totalAmount, String paymentMethod, List<ProductOrderDto> cartItems, Runnable onSuccess, Consumer<Exception> onError) {
        orderRepository.placeOrder(userId, totalAmount, paymentMethod, cartItems, onSuccess, onError);
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

//    public void insert(Order order) {
//        orderRepository.insertOrder(order);
//    }
//
//    public void delete(Order order) {
//        orderRepository.deleteOrder(order);
//    }

}
