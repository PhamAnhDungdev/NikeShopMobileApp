package com.example.nikeshop.data;

import com.example.nikeshop.models.Order;
import java.util.ArrayList;
import java.util.List;

public class OrderDataManager {

    private static OrderDataManager instance;

    private OrderDataManager() {}

    public static OrderDataManager getInstance() {
        if (instance == null) {
            instance = new OrderDataManager();
        }
        return instance;
    }

    // For now, return sample data. Later replace with database calls
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();

        // Sample data
        orders.add(new Order(
                "127123124125110",
                "16 Mar 2022 17:53:42 PM",
                "16 Mar 2022 17:59:05 PM",
                "Nike Air 1",
                "Running Shoes",
                "nike_air_1",
                244.99,
                1,
                "Delivered",
                244.99
        ));

        orders.add(new Order(
                "127123124125102",
                "16 Mar 2022 17:53:42 PM",
                "16 Mar 2022 17:59:05 PM",
                "Nike Air 444",
                "Casual Shoes",
                "nike_air_444",
                244.99,
                1,
                "Delivered",
                244.99
        ));

        return orders;
    }

    // Method for future database integration
    public void addOrder(Order order) {
        // TODO: Add to database
    }

    // Method for future database integration
    public Order getOrderById(String orderId) {
        // TODO: Get from database
        return null;
    }

    // Method for future database integration
    public void updateOrderStatus(String orderId, String status) {
        // TODO: Update in database
    }
}