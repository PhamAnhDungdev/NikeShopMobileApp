package com.example.nikeshop.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(
        tableName = "order_details",
        foreignKeys = {
                @ForeignKey(
                        entity = Order.class,
                        parentColumns = "id",
                        childColumns = "order_id",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = "order_id", name = "index_order_details_order_id")
        }
)
public class OrderDetail {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @NonNull
    @ColumnInfo(name = "order_id")
    private int orderId;

    @ColumnInfo(name = "product_id")
    private int productId;

    @ColumnInfo(name = "product_name")
    private String productName;

    @ColumnInfo(name = "product_size")
    private String productSize;

    @ColumnInfo(name = "product_image_url")
    private String productImageUrl;

    @ColumnInfo(name = "product_brand")
    private String productBrand;

    @ColumnInfo(name = "product_sku")
    private String productSku;

    @ColumnInfo(name = "quantity")
    private int quantity;

    @ColumnInfo(name = "product_price")
    private double productPrice;

    @ColumnInfo(name = "price")
    private double price; // Giá tại thời điểm đặt

    @ColumnInfo(name = "created_at")
    private Date createdAt;

    @ColumnInfo(name = "updated_at")
    private Date updatedAt;

    @ColumnInfo(name = "deleted_at")
    private Date deletedAt;

    // === Constructor không tham số ===
    public OrderDetail() {}

    // === Constructor đầy đủ ===
    @Ignore
    public OrderDetail(@NonNull int orderId,
                       int productId,
                       String productName,
                       String productSize,
                       String productImageUrl,
                       String productBrand,
                       String productSku,
                       int quantity,
                       double productPrice,
                       double price,
                       Date createdAt,
                       Date updatedAt,
                       Date deletedAt) {
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.productSize = productSize;
        this.productImageUrl = productImageUrl;
        this.productBrand = productBrand;
        this.productSku = productSku;
        this.quantity = quantity;
        this.productPrice = productPrice;
        this.price = price;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    // === Getter & Setter ===

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    @NonNull
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(@NonNull int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public double getProductPrice() {
        return productPrice;
    }
    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productSize='" + productSize + '\'' +
                ", productImageUrl='" + productImageUrl + '\'' +
                ", productBrand='" + productBrand + '\'' +
                ", productSku='" + productSku + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                '}';
    }


}
