package com.example.nikeshop.data.local.modelDto;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductOrderDto implements Parcelable {
    public int cartId;
    public int userId;
    public int productId;
    public int quantity;
    public String productName;
    public String productImageUrl;
    public double productPrice;
    public double totalPrice;

    // Constructor mặc định
    public ProductOrderDto() {}

    // Parcelable Constructor
    protected ProductOrderDto(Parcel in) {
        cartId = in.readInt();
        userId = in.readInt();
        productId = in.readInt();
        quantity = in.readInt();
        productName = in.readString();
        productImageUrl = in.readString();
        productPrice = in.readDouble();
        totalPrice = in.readDouble();
    }

    public static final Creator<ProductOrderDto> CREATOR = new Creator<ProductOrderDto>() {
        @Override
        public ProductOrderDto createFromParcel(Parcel in) {
            return new ProductOrderDto(in);
        }

        @Override
        public ProductOrderDto[] newArray(int size) {
            return new ProductOrderDto[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cartId);
        dest.writeInt(userId);
        dest.writeInt(productId);
        dest.writeInt(quantity);
        dest.writeString(productName);
        dest.writeString(productImageUrl);
        dest.writeDouble(productPrice);
        dest.writeDouble(totalPrice);
    }
}
