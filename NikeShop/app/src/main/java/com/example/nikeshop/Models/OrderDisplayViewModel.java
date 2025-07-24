package com.example.nikeshop.Models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nikeshop.data.local.AppDatabase;
import com.example.nikeshop.data.local.dao.CartDao;
import com.example.nikeshop.data.local.dao.OrderDao;
import com.example.nikeshop.data.local.dao.OrderDetailDao;
import com.example.nikeshop.data.local.dao.ProductDao;
import com.example.nikeshop.data.local.entity.Order;
import com.example.nikeshop.data.local.entity.OrderDetail;
import com.example.nikeshop.data.local.entity.Product;
import com.example.nikeshop.data.repositories.OrderDetailRepository;
import com.example.nikeshop.data.repositories.OrderRepository;
import com.example.nikeshop.data.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderDisplayViewModel extends AndroidViewModel {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final MutableLiveData<List<OrderDisplayItem>> displayItems = new MutableLiveData<>();


    public OrderDisplayViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        OrderDao orderDao = db.orderDao();
        OrderDetailDao orderDetailDao = db.orderDetailDao();
        CartDao cartDao = db.cartDao();
        ProductDao productDao = db.productDao();

        this.orderRepository = new OrderRepository(orderDao, orderDetailDao, cartDao, productDao);
        this.orderDetailRepository = new OrderDetailRepository(orderDetailDao);
        this.productRepository = new ProductRepository(productDao);
    }

    public LiveData<List<OrderDisplayItem>> getDisplayItems() {
        return displayItems;
    }

    public void loadDisplayItems() {
        executorService.execute(() -> {
            List<Order> orders = orderRepository.getAllOrdersSync();
            List<OrderDisplayItem> result = new ArrayList<>();

            for (Order order : orders) {
                List<OrderDetail> details = orderDetailRepository.getByOrderIdSync(order.getId());
                for (OrderDetail detail : details) {
                    Product product = productRepository.getProductById(detail.getProductId());
                    if (product != null) {
                        OrderDisplayItem item = new OrderDisplayItem(
                                order.getId(),
                                order.getOrderDate(),
                                product.getName(),
                                product.getBrand(),
                                detail.getProductPrice(),
                                detail.getQuantity(),
                                order.getTotalPrice(),
                                product.getImageUrl(),
                                order.getStatus()
                        );
                        result.add(item);
                    }
                }
            }
            displayItems.postValue(result);
        });
    }
}
