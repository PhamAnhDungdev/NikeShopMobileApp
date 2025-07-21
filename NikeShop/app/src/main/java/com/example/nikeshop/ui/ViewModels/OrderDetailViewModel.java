package com.example.nikeshop.ui.ViewModels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.nikeshop.adapters.OrderDetailAdapter;
import com.example.nikeshop.data.local.AppDatabase;
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

public class OrderDetailViewModel extends AndroidViewModel {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<Order> currentOrder = new MutableLiveData<>();
    private final MutableLiveData<List<OrderDetailAdapter.OrderDetailWithProduct>> orderDetailsWithProducts = new MutableLiveData<>();

    public OrderDetailViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(application);
        OrderDao orderDao = database.orderDao();
        OrderDetailDao orderDetailDao = database.orderDetailDao();
        ProductDao productDao = database.productDao();

        orderRepository = new OrderRepository(orderDao);
        orderDetailRepository = new OrderDetailRepository(orderDetailDao);
        productRepository = new ProductRepository(productDao);
    }

    public LiveData<Order> getCurrentOrder() {
        return currentOrder;
    }

    public LiveData<List<OrderDetailAdapter.OrderDetailWithProduct>> getOrderDetailsWithProducts() {
        return orderDetailsWithProducts;
    }

    public void loadOrderDetails(int orderId) {
        executor.execute(() -> {
            // Load order
            Order order = orderRepository.getOrderById(orderId);
            currentOrder.postValue(order);

            // Load order details with products
            List<OrderDetail> orderDetails = orderDetailRepository.getByOrderIdSync(orderId);
            List<OrderDetailAdapter.OrderDetailWithProduct> detailsWithProducts = new ArrayList<>();

            for (OrderDetail detail : orderDetails) {
                Product product = productRepository.getProductById(detail.getProductId());
                detailsWithProducts.add(new OrderDetailAdapter.OrderDetailWithProduct(detail, product));
            }

            orderDetailsWithProducts.postValue(detailsWithProducts);
        });
    }
}
