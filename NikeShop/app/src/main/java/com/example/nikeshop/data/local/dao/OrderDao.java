package com.example.nikeshop.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.nikeshop.data.local.entity.Order;

import java.util.List;

@Dao
public interface OrderDao {

    @Insert
    List<Long> insertAll(List<Order> orders);
    @Insert
    void insertOrder(Order order);

    @Delete
    void deleteOrder(Order order);
    @Query("SELECT COUNT(*) FROM orders")
    int countOrders();

    @Query("SELECT * FROM orders")
    LiveData<List<Order>> getAllOrders();

    @Query("SELECT * FROM orders WHERE user_id = :userId")
    LiveData<List<Order>> getOrdersByUser(int userId);

    @Query("SELECT * FROM orders WHERE payment_status = :status")
    LiveData<List<Order>> getOrdersByStatus(String status);


    @Query("DELETE FROM orders")
    void deleteAllOrders();
}
