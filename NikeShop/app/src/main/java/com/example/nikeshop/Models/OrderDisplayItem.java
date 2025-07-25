package com.example.nikeshop.Models;

import java.util.Date;

public class OrderDisplayItem {
    private final int orderId;
    private final Date placedDate;
    private final String productName;
    private final String productCategory;
    private final double productPrice;
    private final int quantity;
    private final double totalPrice;
    private final String productImage;
    private final String status;

    public OrderDisplayItem(int orderId, Date placedDate, String productName, String productCategory,
                            double productPrice, int quantity, double totalPrice, String productImage, String status) {
        this.orderId = orderId;
        this.placedDate = placedDate;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.productImage = productImage;
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public Date getPlacedDate() {
        return placedDate;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getStatus() {
        return status;
    }
}
