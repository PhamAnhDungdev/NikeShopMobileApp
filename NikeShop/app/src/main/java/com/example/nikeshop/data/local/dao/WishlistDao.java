package com.example.nikeshop.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.nikeshop.data.local.entity.Wishlist;

import java.util.List;

@Dao
public interface WishlistDao {
    @Insert
    List<Long> insertAll(List<Wishlist> wishlists);
}
