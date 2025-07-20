package com.example.nikeshop.data.local.dao;

import androidx.room.Dao;

import com.example.nikeshop.data.local.entity.Cart;

import java.util.List;

@Dao
public interface CartDao {
    @Insert
    void insertAll(List<Cart> carts);
}
