package com.example.nikeshop.Models;

public class ProductFavourite {
    private String name;
    private String type;
    private String price;
    private int imageResId;

    public ProductFavourite(String name, String type, String price, int imageResId) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.imageResId = imageResId;
    }

    public String getName() { return name; }
    public String getType() { return type; }
    public String getPrice() { return price; }
    public int getImageResId() { return imageResId; }
}
