package com.example.nikeshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nikeshop.Models.OrderDisplayItem;
import com.example.nikeshop.R;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderDisplayAdapter extends RecyclerView.Adapter<OrderDisplayAdapter.OrderViewHolder> {

    private final Context context;
    private List<OrderDisplayItem> orderList;
    private OnOrderClickListener listener;

    // ✅ Interface dùng để truyền callback khi click vào đơn hàng
    public interface OnOrderClickListener {
        void onOrderClick(OrderDisplayItem orderItem);
    }

    public OrderDisplayAdapter(Context context, List<OrderDisplayItem> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    public void setOnOrderClickListener(OnOrderClickListener listener) {
        this.listener = listener;
    }

    public void updateOrders(List<OrderDisplayItem> newOrders) {
        this.orderList = newOrders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_display, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderDisplayItem item = orderList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderNumber, tvDate, tvProductName, tvProductCategory, tvPrice, tvQuantity, tvStatus;
        ImageView ivProductImage;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderNumber = itemView.findViewById(R.id.tv_order_number);
            tvDate = itemView.findViewById(R.id.tv_placed_date);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductCategory = itemView.findViewById(R.id.tv_product_category);
            tvPrice = itemView.findViewById(R.id.tv_product_price);
            tvQuantity = itemView.findViewById(R.id.tv_product_quantity);
            tvStatus = itemView.findViewById(R.id.tv_order_status);
            ivProductImage = itemView.findViewById(R.id.iv_product_image);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (listener != null && pos != RecyclerView.NO_POSITION) {
                    listener.onOrderClick(orderList.get(pos));
                }
            });
        }

        public void bind(OrderDisplayItem item) {
            tvOrderNumber.setText("Order #" + item.getOrderId());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            tvDate.setText("Date: " + sdf.format(item.getPlacedDate()));

            tvProductName.setText(item.getProductName());
            tvProductCategory.setText(item.getProductCategory());
            tvQuantity.setText("x" + item.getQuantity());

            NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
            tvPrice.setText(currency.format(item.getTotalPrice()));

            tvStatus.setText(item.getStatus());

            Glide.with(context)
                    .load(item.getProductImage())
                    .placeholder(R.drawable.placeholder_shoe)
                    .into(ivProductImage);
        }
    }
}
