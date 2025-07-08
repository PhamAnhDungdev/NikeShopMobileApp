package com.example.nikeshop.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(
        tableName = "reviews",
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "id",
                        childColumns = "user_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Product.class,
                        parentColumns = "id",
                        childColumns = "product_id",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = "user_id", name = "index_reviews_user_id"),
                @Index(value = "product_id", name = "index_reviews_product_id")
        }
)
public class Review {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @NonNull
    @ColumnInfo(name = "user_id")
    public int userId;

    @NonNull
    @ColumnInfo(name = "product_id")
    public int productId;

    @ColumnInfo(name = "rating")
    public int rating; // 1-5

    @ColumnInfo(name = "comment")
    public String comment;

    @NonNull
    @ColumnInfo(name = "review_date")
    public Date reviewDate; // Sửa sang Date để dễ xử lý thời gian

    @ColumnInfo(name = "created_at")
    public Date createdAt;

    @ColumnInfo(name = "updated_at")
    public Date updatedAt;

    @ColumnInfo(name = "deleted_at")
    public Date deletedAt;
}
