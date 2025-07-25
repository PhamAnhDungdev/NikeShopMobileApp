package com.example.nikeshop.ui.ViewModels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.nikeshop.adapters.OrderDetailAdapter;
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

public class OrderDetailViewModel extends AndroidViewModel {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<Order> currentOrder = new MutableLiveData<>();
    private final MutableLiveData<List<OrderDetailAdapter.OrderDetailWithProduct>> orderDetailsWithProducts = new MutableLiveData<>();
    private final MutableLiveData<Double> totalAmount = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public OrderDetailViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(application);
        OrderDao orderDao = database.orderDao();
        OrderDetailDao orderDetailDao = database.orderDetailDao();
        CartDao cartDao = database.cartDao();
        ProductDao productDao = database.productDao();

        orderRepository = new OrderRepository(orderDao, orderDetailDao, cartDao);
        orderDetailRepository = new OrderDetailRepository(orderDetailDao);
        productRepository = new ProductRepository(productDao);
    }

    public LiveData<Order> getCurrentOrder() {
        return currentOrder;
    }

    public LiveData<List<OrderDetailAdapter.OrderDetailWithProduct>> getOrderDetailsWithProducts() {
        return orderDetailsWithProducts;
    }

    public LiveData<Double> getTotalAmount() {
        return totalAmount;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void loadOrderDetails(int orderId) {
        // Hiển thị loading
        isLoading.postValue(true);

        // Thực hiện database operations trên background thread
        executor.execute(() -> {
            try {
                // Load thông tin order
                Order order = orderRepository.getOrderByIdSync(orderId);
                currentOrder.postValue(order);

                // Load order details với products
                List<OrderDetail> orderDetails = orderDetailRepository.getByOrderIdSync(orderId);
                List<OrderDetailAdapter.OrderDetailWithProduct> detailsWithProducts = new ArrayList<>();
                double total = 0.0;

                if (orderDetails != null && !orderDetails.isEmpty()) {
                    for (OrderDetail detail : orderDetails) {
                        Product product = productRepository.getProductById(detail.getProductId());
                        if (product != null) {
                            detailsWithProducts.add(new OrderDetailAdapter.OrderDetailWithProduct(detail, product));
                            total += detail.getPrice() * detail.getQuantity();
                        }
                    }
                }

                // Gửi kết quả về main thread
                orderDetailsWithProducts.postValue(detailsWithProducts);
                totalAmount.postValue(total);

            } catch (Exception e) {
                e.printStackTrace();
                // Xử lý lỗi - gửi danh sách rỗng
                orderDetailsWithProducts.postValue(new ArrayList<>());
            } finally {
                // Ẩn loading
                isLoading.postValue(false);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}
