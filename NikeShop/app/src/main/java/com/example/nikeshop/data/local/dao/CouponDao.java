package com.example.nikeshop.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.nikeshop.data.local.entity.Coupon;

import java.util.List;

@Dao
public interface CouponDao {
    @Insert
    List<Long> insertAll(List<Coupon> coupons);
    @Query("SELECT COUNT(*) FROM coupons")
    int countCoupons();
}
