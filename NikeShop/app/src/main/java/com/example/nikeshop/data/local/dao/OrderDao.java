package com.example.nikeshop.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.nikeshop.data.local.entity.Order;

import java.util.List;

@Dao
public interface OrderDao {

    @Insert
    void insert(Order order);

    @Update
    void update(Order order);

    @Delete
    void delete(Order order);

    @Query("SELECT * FROM orders WHERE deleted_at IS NULL ORDER BY order_date DESC")
    LiveData<List<Order>> getAllOrders();

    @Query("SELECT * FROM orders WHERE deleted_at IS NULL AND " +
            "(CAST(user_id AS TEXT) LIKE '%' || :keyword || '%' " +
            "OR payment_status LIKE '%' || :keyword || '%') " +
            "ORDER BY order_date DESC")
    LiveData<List<Order>> searchOrders(String keyword);

    @Query("SELECT * FROM orders WHERE payment_status = :status")
    LiveData<List<Order>> getOrdersByStatus(String status);

    @Query("SELECT * FROM orders WHERE id = :id LIMIT 1")
    LiveData<Order> getOrderById(int id);



}
