package com.example.nikeshop.models;

public class Order {
    private String orderNumber;
    private String placedDate;
    private String paidDate;
    private String productName;
    private String productCategory;
    private String productImage;
    private double productPrice;
    private int quantity;
    private String status;
    private double totalPrice;

    // Constructor
    public Order(String orderNumber, String placedDate, String paidDate,
                 String productName, String productCategory, String productImage,
                 double productPrice, int quantity, String status, double totalPrice) {
        this.orderNumber = orderNumber;
        this.placedDate = placedDate;
        this.paidDate = paidDate;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    // Getters
    public String getOrderNumber() { return orderNumber; }
    public String getPlacedDate() { return placedDate; }
    public String getPaidDate() { return paidDate; }
    public String getProductName() { return productName; }
    public String getProductCategory() { return productCategory; }
    public String getProductImage() { return productImage; }
    public double getProductPrice() { return productPrice; }
    public int getQuantity() { return quantity; }
    public String getStatus() { return status; }
    public double getTotalPrice() { return totalPrice; }

    // Setters (for future database updates)
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public void setPlacedDate(String placedDate) { this.placedDate = placedDate; }
    public void setPaidDate(String paidDate) { this.paidDate = paidDate; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setProductCategory(String productCategory) { this.productCategory = productCategory; }
    public void setProductImage(String productImage) { this.productImage = productImage; }
    public void setProductPrice(double productPrice) { this.productPrice = productPrice; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setStatus(String status) { this.status = status; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
}