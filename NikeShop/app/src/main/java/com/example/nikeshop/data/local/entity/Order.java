package com.example.nikeshop.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
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
    public int id;

    @NonNull
    @ColumnInfo(name = "user_id")
    public int userId;

    @NonNull
    @ColumnInfo(name = "order_date")
    public Date orderDate; // Chuyển sang Date để dễ xử lý, nếu muốn giữ String hãy thông báo mình sửa lại

    @ColumnInfo(name = "total_price")
    public double totalPrice;

    @NonNull
    @ColumnInfo(name = "payment_status")
    public String status; // pending, completed, canceled

    @ColumnInfo(name = "payment_method")
    public String paymentMethod;

    @ColumnInfo(name = "created_at")
    public Date createdAt;

    @ColumnInfo(name = "updated_at")
    public Date updatedAt;

    @ColumnInfo(name = "deleted_at")
    public Date deletedAt;
}
