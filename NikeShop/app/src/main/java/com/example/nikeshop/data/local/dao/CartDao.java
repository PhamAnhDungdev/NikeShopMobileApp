package com.example.nikeshop.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.nikeshop.data.local.entity.Cart;

import java.util.List;

@Dao
public interface CartDao {
    @Insert
    List<Long> insertAll(List<Cart> carts);
}
