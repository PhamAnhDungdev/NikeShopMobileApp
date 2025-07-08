package com.example.nikeshop.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(
        tableName = "products",
        foreignKeys = @ForeignKey(
                entity = Category.class,
                parentColumns = "id",
                childColumns = "category_id",
                onDelete = ForeignKey.CASCADE
        ),
        indices = @Index(value = "category_id", name = "index_product_category_id")
)
public class Product {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @NonNull
    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "price")
    public double price;

    @ColumnInfo(name = "size")
    public String size;

    @ColumnInfo(name = "stock_quantity")
    public int stockQuantity;

    @ColumnInfo(name = "image_url")
    public String imageUrl;

    @NonNull
    @ColumnInfo(name = "category_id")
    public int categoryId;

    // Bổ sung trường quản lý chuyên shop giày chính hãng:
    @ColumnInfo(name = "brand")
    public String brand; // Nike, Adidas, Puma...

    @ColumnInfo(name = "color")
    public String color;

    @ColumnInfo(name = "material")
    public String material; // Da thật, vải, synthetic

    @ColumnInfo(name = "sku")
    public String sku; // Mã quản lý kho

    @ColumnInfo(name = "created_at")
    public Date createdAt;

    @ColumnInfo(name = "updated_at")
    public Date updatedAt;

    @ColumnInfo(name = "deleted_at")
    public Date deletedAt;
}
