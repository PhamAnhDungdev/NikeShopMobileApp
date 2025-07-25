package com.example.nikeshop.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.nikeshop.data.local.entity.OrderDetail;
import com.example.nikeshop.data.local.modelDto.TopSellingProductDto;

import java.util.List;

@Dao
public interface OrderDetailDao {

    @Insert
    Long insert(OrderDetail orderDetail);

    @Insert
    List<Long> insertAll(List<OrderDetail> orderDetails);

    @Delete
    void delete(OrderDetail orderDetail);

    @Query("SELECT COUNT(*) FROM order_details")
    int countOrderDetails();

    @Query("SELECT * FROM order_details WHERE deleted_at IS NULL")
    LiveData<List<OrderDetail>> getAll();

    @Query("SELECT * FROM order_details WHERE order_id = :orderId AND deleted_at IS NULL")
    LiveData<List<OrderDetail>> getByOrderId(int orderId);

    // Synchronous method for ViewModel
    @Query("SELECT * FROM order_details WHERE order_id = :orderId AND deleted_at IS NULL")
    List<OrderDetail> getByOrderIdSync(int orderId);

    @Query("DELETE FROM order_details")
    void deleteAll();

    @Query("SELECT product_id AS productId, SUM(quantity) AS totalSold " +
            "FROM order_details " +
            "WHERE deleted_at IS NULL " +
            "GROUP BY product_id " +
            "ORDER BY totalSold DESC " +
            "LIMIT :limit")
    List<TopSellingProductDto> getTopSellingProducts(int limit);

}
