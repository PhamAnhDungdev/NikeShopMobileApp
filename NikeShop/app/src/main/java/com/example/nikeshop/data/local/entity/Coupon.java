package com.example.nikeshop.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "coupons")
public class Coupon {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @NonNull
    @ColumnInfo(name = "code")
    public String code; // Mã giảm giá, ví dụ: SUMMER2025

    @ColumnInfo(name = "discount_percentage")
    public double discountPercentage; // %

    @ColumnInfo(name = "max_discount_amount")
    public double maxDiscountAmount; // Giảm tối đa bao nhiêu VND

    @NonNull
    @ColumnInfo(name = "expiry_date")
    public Date expiryDate; // Date để dễ so sánh

    @ColumnInfo(name = "is_active")
    public boolean isActive; // true nếu đang hoạt động

    @ColumnInfo(name = "created_at")
    public Date createdAt;

    @ColumnInfo(name = "updated_at")
    public Date updatedAt;

    @ColumnInfo(name = "deleted_at")
    public Date deletedAt;
}
