package com.example.nikeshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nikeshop.R;
import com.example.nikeshop.data.local.entity.OrderDetail;
import com.example.nikeshop.data.local.entity.Product;
import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {

    public interface OnRatingClickListener {
        void onRatingClick(OrderDetail orderDetail, Product product);
    }

    private Context context;
    private List<OrderDetailWithProduct> orderDetails;
    private OnRatingClickListener onRatingClickListener;

    public OrderDetailAdapter(Context context, List<OrderDetailWithProduct> orderDetails) {
        this.context = context;
        this.orderDetails = orderDetails;
    }

    public void setOnRatingClickListener(OnRatingClickListener listener) {
        this.onRatingClickListener = listener;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_detail, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        OrderDetailWithProduct item = orderDetails.get(position);
        holder.bind(item, onRatingClickListener);
    }

    @Override
    public int getItemCount() {
        return orderDetails != null ? orderDetails.size() : 0;
    }

    public void updateOrderDetails(List<OrderDetailWithProduct> newOrderDetails) {
        this.orderDetails = newOrderDetails;
        notifyDataSetChanged();
    }

    public static class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductImage;
        private TextView tvProductName;
        private TextView tvProductCategory;
        private TextView tvProductPrice;
        private TextView tvQuantity;
        private TextView tvSubtotal;
        private Button btnRating;

        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.iv_product_image);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductCategory = itemView.findViewById(R.id.tv_product_category);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            tvSubtotal = itemView.findViewById(R.id.tv_subtotal);
            btnRating = itemView.findViewById(R.id.btn_rating);
        }

        public void bind(OrderDetailWithProduct item, OnRatingClickListener listener) {
            OrderDetail orderDetail = item.orderDetail;
            Product product = item.product;

            if (product != null) {
                tvProductName.setText(product.getName());
                // Sử dụng brand thay vì category, hoặc có thể hiển thị "Category ID: " + categoryId
                tvProductCategory.setText(product.getBrand() != null ? product.getBrand() : "Brand N/A");
                // Load product image here if you have image loading library like Glide or Picasso
                 Glide.with(itemView.getContext()).load(product.getImageUrl()).into(ivProductImage);
            }

            tvProductPrice.setText(String.format("$%.2f", orderDetail.getPrice()));
            tvQuantity.setText(String.format("x%d", orderDetail.getQuantity()));
            tvSubtotal.setText(String.format("$%.2f", orderDetail.getPrice() * orderDetail.getQuantity()));

            // Handle rating button click
            btnRating.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRatingClick(orderDetail, product);
                }
            });
        }
    }

    // Helper class to combine OrderDetail with Product
    public static class OrderDetailWithProduct {
        public OrderDetail orderDetail;
        public Product product;

        public OrderDetailWithProduct(OrderDetail orderDetail, Product product) {
            this.orderDetail = orderDetail;
            this.product = product;
        }
    }
}
