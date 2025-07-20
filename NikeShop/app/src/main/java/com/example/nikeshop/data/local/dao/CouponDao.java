package com.example.nikeshop.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.nikeshop.data.local.entity.Coupon;

import java.util.List;

@Dao
public interface CouponDao {
    @Insert
    void insertAll(List<Coupon> coupons);
}
