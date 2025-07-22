package com.example.nikeshop.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.nikeshop.data.local.entity.Wishlist;

import java.util.List;

@Dao
public interface WishlistDao {

    @Insert
    long insert(Wishlist wishlist);

    @Insert
    List<Long> insertAll(List<Wishlist> wishlists);

    @Query("SELECT COUNT(*) FROM wishlists")
    int countWishlists();

    @Query("SELECT COUNT(*) FROM wishlists WHERE user_id = :userId AND product_id = :productId")
    int exists(int userId, int productId);
}
