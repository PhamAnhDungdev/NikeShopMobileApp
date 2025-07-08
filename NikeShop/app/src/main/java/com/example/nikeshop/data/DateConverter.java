package com.example.nikeshop.data;

import androidx.room.TypeConverter;
import java.util.Date;

public class DateConverter {

    // Chuyển Date thành Long (milliseconds)
    @TypeConverter
    public static Long fromDate(Date date) {
        return date == null ? null : date.getTime();
    }

    // Chuyển Long (milliseconds) thành Date
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }
}