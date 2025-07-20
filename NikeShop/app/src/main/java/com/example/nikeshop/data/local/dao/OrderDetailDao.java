package com.example.nikeshop.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.nikeshop.data.local.entity.OrderDetail;

import java.util.List;

@Dao
public interface OrderDetailDao {

    @Insert
    void insert(OrderDetail orderDetail);

    @Insert
    void insertAll(List<OrderDetail> orderDetails);

    @Delete
    void delete(OrderDetail orderDetail);

    @Query("SELECT * FROM order_details")
    LiveData<List<OrderDetail>> getAll();

    @Query("SELECT * FROM order_details WHERE order_id = :orderId")
    LiveData<List<OrderDetail>> getByOrderId(int orderId);

    @Query("DELETE FROM order_details")
    void deleteAll();
}
