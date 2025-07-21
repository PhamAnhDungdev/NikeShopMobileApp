package com.example.nikeshop.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.nikeshop.data.local.entity.Product;
import com.example.nikeshop.data.local.entity.Wishlist;

import java.util.List;

@Dao
public interface WishlistDao {
    @Insert
    List<Long> insertAll(List<Wishlist> wishlists);

    @Query("SELECT COUNT(*) FROM wishlists")
    int countWishlists();

    // Thêm 1 item
    @Insert
    long insert(Wishlist wishlist);

    // Lấy wishlist theo userId
    @Query("SELECT * FROM wishlists WHERE user_id = :userId")
    List<Wishlist> getWishlistByUserId(int userId);

    // Xoá 1 item theo userId và productId
    @Query("DELETE FROM wishlists WHERE user_id = :userId AND product_id = :productId")
    void deleteByUserAndProduct(int userId, int productId);

    // Xoá toàn bộ wishlist của user
    @Query("DELETE FROM wishlists WHERE user_id = :userId")
    void deleteAllByUser(int userId);

    @Query("SELECT products.* FROM wishlists INNER JOIN products ON wishlists.product_id = products.id WHERE wishlists.user_id = :userId")
    LiveData<List<Product>> getProductsInWishlistByUserId(int userId);

    @Query("SELECT * FROM wishlists")
    LiveData<List<Wishlist>> getAllWishlistItems();
}
