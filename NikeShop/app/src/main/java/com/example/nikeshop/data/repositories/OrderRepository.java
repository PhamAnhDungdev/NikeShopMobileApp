package com.example.nikeshop.data.repositories;

import androidx.lifecycle.LiveData;

import com.example.nikeshop.data.local.dao.CartDao;
import com.example.nikeshop.data.local.dao.OrderDao;
import com.example.nikeshop.data.local.dao.OrderDetailDao;
import com.example.nikeshop.data.local.entity.Order;
import com.example.nikeshop.data.local.entity.OrderDetail;
import com.example.nikeshop.data.local.modelDto.ProductOrderDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class OrderRepository {
    private final OrderDao orderDao;
    private final OrderDetailDao orderDetailDao;
    private final CartDao cartDao;
    private final ExecutorService executorService;
    public OrderRepository(OrderDao orderDao, OrderDetailDao orderDetailDao, CartDao cartDao) {
        this.orderDao = orderDao;
        this.orderDetailDao = orderDetailDao;
        this.cartDao = cartDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void placeOrder(int userId, double totalAmount, String paymentMethod, List<ProductOrderDto> cartItems, Runnable onSuccess, Consumer<Exception> onError) {
        new Thread(() -> {
            try {
                Date now = new Date();
                Order order = new Order(
                        userId,
                        now,
                        totalAmount,
                        "pending",
                        paymentMethod,
                        now,
                        now,
                        null
                );
                long orderId = orderDao.insertOrder(order);

                List<OrderDetail> details = new ArrayList<>();
                for (ProductOrderDto item : cartItems) {
                    OrderDetail detail = new OrderDetail(
                            (int) orderId,
                            item.productId,
                            item.productName,
                            null,
                            item.productImageUrl,
                            null,
                            null,
                            item.quantity,
                            item.productPrice,
                            item.totalPrice,
                            now,
                            now,
                            null
                    );
                    details.add(detail);
                }
                orderDetailDao.insertAll(details);
                cartDao.clearCartByUserId(userId);
                if (onSuccess != null) onSuccess.run();
            } catch (Exception e) {
                if (onError != null) onError.accept(e);
            }
        }).start();
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
