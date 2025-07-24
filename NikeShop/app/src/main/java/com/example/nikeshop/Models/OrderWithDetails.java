package com.example.nikeshop.Models;

import com.example.nikeshop.data.local.entity.Order;

import java.util.List;

public class OrderWithDetails {
    private final Order order;
    private final List<OrderProductItem> products;

    public OrderWithDetails(Order order, List<OrderProductItem> products) {
        this.order = order;
        this.products = products;
    }

    public Order getOrder() {
        return order;
    }

    public List<OrderProductItem> getProducts() {
        return products;
    }
}


