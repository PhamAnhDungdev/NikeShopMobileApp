package com.example.nikeshop.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nikeshop.Models.OrderDisplayItem;
import com.example.nikeshop.data.local.AppDatabase;
import com.example.nikeshop.data.local.dao.CartDao;
import com.example.nikeshop.data.local.dao.OrderDao;
import com.example.nikeshop.data.local.dao.OrderDetailDao;
import com.example.nikeshop.data.local.dao.ProductDao;
import com.example.nikeshop.data.local.entity.Order;
import com.example.nikeshop.data.local.entity.OrderDetail;
import com.example.nikeshop.data.local.entity.Product;
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
    private final ProductDao productDao; // optional, can be null
    private final ExecutorService executorService;

    public OrderRepository(OrderDao orderDao, OrderDetailDao orderDetailDao, CartDao cartDao, ProductDao productDao) {
        this.orderDao = orderDao;
        this.orderDetailDao = orderDetailDao;
        this.cartDao = cartDao;
        this.productDao = productDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public OrderRepository(OrderDao orderDao, OrderDetailDao orderDetailDao, CartDao cartDao) {
        this(orderDao, orderDetailDao, cartDao, null);
    }

    public List<Order> getAllOrdersSync() {
        return orderDao.getAllOrdersSync();
    }


    public void placeOrder(int userId, double totalAmount, String paymentMethod,
                           List<ProductOrderDto> cartItems,
                           Runnable onSuccess,
                           Consumer<Exception> onError) {
        executorService.execute(() -> {
            try {
                Date now = new Date();
                Order order = new Order(userId, now, totalAmount, "pending", paymentMethod, now, now, null);
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
        });
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

    public LiveData<Order> getOrderById(int orderId) {
        return orderDao.getOrderByIdLive(orderId);
    }

    public void insert(Order order) {
        executorService.execute(() -> orderDao.insertOrder(order));
    }

    public void update(Order order) {
        executorService.execute(() -> {
            order.setUpdatedAt(new Date());
            orderDao.updateOrder(order);
        });
    }

    public void delete(Order order) {
        executorService.execute(() -> orderDao.deleteOrder(order));
    }

    public LiveData<List<Order>> search(String keyword) {
        return orderDao.searchOrders(keyword);
    }

    public void deleteAllOrders() {
        executorService.execute(orderDao::deleteAllOrders);
    }

    public Order getOrderByIdSync(int orderId) {
        return orderDao.getOrderById(orderId);
    }

    public LiveData<List<OrderDisplayItem>> getAllOrderDisplays() {
        MutableLiveData<List<OrderDisplayItem>> result = new MutableLiveData<>();
        executorService.execute(() -> {
            List<Order> orders = orderDao.getAllOrdersSync();
            List<OrderDisplayItem> displayItems = new ArrayList<>();

            for (Order order : orders) {
                List<OrderDetail> details = orderDetailDao.getByOrderIdSync(order.getId());
                for (OrderDetail detail : details) {
                    Product product = productDao.getProductById(detail.getProductId());
                    if (product != null) {
                        OrderDisplayItem display = new OrderDisplayItem(
                                order.getId(),
                                order.getOrderDate(),
                                product.getName(),
                                "Category", // Optional: replace with real category if needed
                                detail.getProductPrice(),
                                detail.getQuantity(),
                                order.getTotalPrice(),
                                product.getImageUrl(),
                                order.getStatus()
                        );
                        displayItems.add(display);
                    }
                }
            }
            result.postValue(displayItems);
        });
        return result;
    }

    public LiveData<List<OrderDisplayItem>> getDisplayByStatus(String status) {
        MutableLiveData<List<OrderDisplayItem>> result = new MutableLiveData<>();
        executorService.execute(() -> {
            List<Order> orders = orderDao.getOrdersByStatusSync(status);
            List<OrderDisplayItem> displayItems = new ArrayList<>();
            for (Order order : orders) {
                List<OrderDetail> details = orderDetailDao.getByOrderIdSync(order.getId());
                for (OrderDetail detail : details) {
                    Product product = productDao.getProductById(detail.getProductId());
                    if (product != null) {
                        OrderDisplayItem display = new OrderDisplayItem(
                                order.getId(),
                                order.getOrderDate(),
                                product.getName(),
                                "Category",
                                detail.getProductPrice(),
                                detail.getQuantity(),
                                order.getTotalPrice(),
                                product.getImageUrl(),
                                order.getStatus()
                        );
                        displayItems.add(display);
                    }
                }
            }
            result.postValue(displayItems);
        });
        return result;
    }

    public LiveData<List<OrderDisplayItem>> searchDisplay(String keyword) {
        MutableLiveData<List<OrderDisplayItem>> result = new MutableLiveData<>();
        executorService.execute(() -> {
            List<Order> orders = orderDao.searchOrdersSync(keyword);
            List<OrderDisplayItem> displayItems = new ArrayList<>();
            for (Order order : orders) {
                List<OrderDetail> details = orderDetailDao.getByOrderIdSync(order.getId());
                for (OrderDetail detail : details) {
                    Product product = productDao.getProductById(detail.getProductId());
                    if (product != null) {
                        OrderDisplayItem display = new OrderDisplayItem(
                                order.getId(),
                                order.getOrderDate(),
                                product.getName(),
                                "Category",
                                detail.getProductPrice(),
                                detail.getQuantity(),
                                order.getTotalPrice(),
                                product.getImageUrl(),
                                order.getStatus()
                        );
                        displayItems.add(display);
                    }
                }
            }
            result.postValue(displayItems);
        });
        return result;
    }

    public void insertOrder(Order order) {
    }

    public void deleteOrder(Order order) {
    }
}