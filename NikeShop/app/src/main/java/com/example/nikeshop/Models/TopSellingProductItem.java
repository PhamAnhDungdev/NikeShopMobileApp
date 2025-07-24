package com.example.nikeshop.Models;

public class TopSellingProductItem {
    public int productId;
    public String name;
    public String imageUrl;
    public double price;
    public int totalSold;
    public double totalRevenue;

    public TopSellingProductItem(int productId, String name, String imageUrl, double price, int totalSold) {
        this.productId = productId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.totalSold = totalSold;
        this.totalRevenue = price * totalSold;    }
}
