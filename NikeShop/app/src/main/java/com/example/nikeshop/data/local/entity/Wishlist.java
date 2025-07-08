package com.example.nikeshop.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(
        tableName = "wishlists",
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
                @Index(value = "user_id", name = "index_wishlist_user_id"),
                @Index(value = "product_id", name = "index_wishlist_product_id")
        }
)
public class Wishlist {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @NonNull
    @ColumnInfo(name = "user_id")
    private int userId;

    @NonNull
    @ColumnInfo(name = "product_id")
    private int productId;

    @ColumnInfo(name = "created_at")
    private Date createdAt;

    @ColumnInfo(name = "updated_at")
    private Date updatedAt;

    @ColumnInfo(name = "deleted_at")
    private Date deletedAt;

    // === Constructor không tham số ===
    public Wishlist() {}

    // === Constructor đầy đủ ===
    public Wishlist(@NonNull int userId,
                    @NonNull int productId,
                    Date createdAt,
                    Date updatedAt,
                    Date deletedAt) {
        this.userId = userId;
        this.productId = productId;
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
    public int getProductId() {
        return productId;
    }

    public void setProductId(@NonNull int productId) {
        this.productId = productId;
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
        return "Wishlist{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                '}';
    }
}
