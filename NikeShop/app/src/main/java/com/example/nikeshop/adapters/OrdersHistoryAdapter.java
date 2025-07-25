package com.example.nikeshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class OrdersHistoryAdapter extends RecyclerView.Adapter<OrdersHistoryAdapter.OrderViewHolder> {

    private final Context context;
    private List<OrderDisplayItem> orderList;
    private OnOrderClickListener clickListener;

    public interface OnOrderClickListener {
        void onOrderClick(OrderDisplayItem order);
    }

    public void setOnOrderClickListener(OnOrderClickListener listener) {
        this.clickListener = listener;
    }

    public OrdersHistoryAdapter(Context context, List<OrderDisplayItem> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    public void updateOrders(List<OrderDisplayItem> newOrders) {
        this.orderList = newOrders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_oder, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderDisplayItem order = orderList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderNumber, tvPlacedDate, tvPaidDate;
        TextView tvProductName, tvProductCategory, tvProductPrice, tvProductQuantity;
        TextView tvOrderStatus, tvItemCount, tvTotalPrice;
        ImageView ivProductImage;
        Button btnView, btnUpdate;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOrderNumber = itemView.findViewById(R.id.tv_order_number);
            tvPlacedDate = itemView.findViewById(R.id.tv_placed_date);
            tvPaidDate = itemView.findViewById(R.id.tv_paid_date);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductCategory = itemView.findViewById(R.id.tv_product_category);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            tvProductQuantity = itemView.findViewById(R.id.tv_product_quantity);
            tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
            tvItemCount = itemView.findViewById(R.id.tv_item_count);
            tvTotalPrice = itemView.findViewById(R.id.tv_total_price);
            ivProductImage = itemView.findViewById(R.id.iv_product_image);
            btnView = itemView.findViewById(R.id.btn_view_order);
            btnUpdate = itemView.findViewById(R.id.btn_update_status);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (clickListener != null && position != RecyclerView.NO_POSITION) {
                    clickListener.onOrderClick(orderList.get(position));
                }
            });
        }

        public void bind(OrderDisplayItem order) {
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            tvOrderNumber.setText("Order #" + order.getOrderId());
            tvPlacedDate.setText("Placed on " + dateFormat.format(order.getPlacedDate()));
            tvPaidDate.setVisibility(View.GONE); // no paid date in OrderDisplayItem

            tvProductName.setText(order.getProductName());
            tvProductCategory.setText(order.getProductCategory());
            tvProductPrice.setText(currencyFormat.format(order.getProductPrice()));
            tvTotalPrice.setText(currencyFormat.format(order.getTotalPrice()));
            tvProductQuantity.setText("x" + order.getQuantity());
            tvOrderStatus.setText(order.getStatus());
            tvItemCount.setText(order.getQuantity() + " item(s), Total:");

            Glide.with(context)
                    .load(order.getProductImage())
                    .placeholder(R.drawable.placeholder_shoe)
                    .into(ivProductImage);

            setStatusBackground(order.getStatus());
        }

        private void setStatusBackground(String status) {
            switch (status.toLowerCase()) {
                case "delivered":
                    tvOrderStatus.setBackgroundResource(R.drawable.status_delivered_bg);
                    break;
                case "shipped":
                    tvOrderStatus.setBackgroundResource(R.drawable.status_shipped_bg);
                    break;
                case "processing":
                    tvOrderStatus.setBackgroundResource(R.drawable.status_processing_bg);
                    break;
                default:
                    tvOrderStatus.setBackgroundResource(R.drawable.status_delivered_bg);
                    break;
            }
        }
    }
}
