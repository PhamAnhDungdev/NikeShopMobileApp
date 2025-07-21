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
        tableName = "orders",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE
        ),
        indices = @Index(value = "user_id", name = "index_order_user_id")
)
public class Order {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @NonNull
    @ColumnInfo(name = "user_id")
    private int userId;

    @NonNull
    @ColumnInfo(name = "order_date")
    private Date orderDate;

    @ColumnInfo(name = "total_price")
    private double totalPrice;

    @NonNull
    @ColumnInfo(name = "payment_status")
    private String status; // pending, completed, canceled

    @ColumnInfo(name = "payment_method")
    private String paymentMethod;

    @ColumnInfo(name = "created_at")
    private Date createdAt;

    @ColumnInfo(name = "updated_at")
    private Date updatedAt;

    @ColumnInfo(name = "deleted_at")
    private Date deletedAt;

    // === Constructor không tham số (Room yêu cầu) ===
    public Order() {}

    // === Constructor đầy đủ ===
    @Ignore
    public Order(@NonNull int userId,
                 @NonNull Date orderDate,
                 double totalPrice,
                 @NonNull String status,
                 String paymentMethod,
                 Date createdAt,
                 Date updatedAt,
                 Date deletedAt) {
        this.userId = userId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.paymentMethod = paymentMethod;
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
    public int getUserId() {
        return userId;
    }

    public void setUserId(@NonNull int userId) {
        this.userId = userId;
    }

    @NonNull
    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(@NonNull Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @NonNull
    public String getStatus() {
        return status;
    }

    public void setStatus(@NonNull String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
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
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", orderDate=" + orderDate +
                ", totalPrice=" + totalPrice +
                ", status='" + status + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                '}';
    }
}
