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

import com.example.nikeshop.R;
import com.example.nikeshop.models.Order;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrdersHistoryAdapter extends RecyclerView.Adapter<OrdersHistoryAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;
    private OnOrderClickListener clickListener;

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public void setOnOrderClickListener(OnOrderClickListener listener) {
        this.clickListener = listener;
    }

    public OrdersHistoryAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_oder, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void updateOrders(List<Order> newOrders) {
        this.orderList = newOrders;
        notifyDataSetChanged();
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

        public void bind(Order order) {
            tvOrderNumber.setText("Order " + order.getOrderNumber());
            tvPlacedDate.setText("Placed on " + order.getPlacedDate());
            tvPaidDate.setText("Paid on " + order.getPaidDate());
            tvProductName.setText(order.getProductName());
            tvProductCategory.setText(order.getProductCategory());

            NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
            tvProductPrice.setText(formatter.format(order.getProductPrice()));
            tvTotalPrice.setText(formatter.format(order.getTotalPrice()));

            tvProductQuantity.setText("X" + order.getQuantity());
            tvOrderStatus.setText(order.getStatus());
            tvItemCount.setText(order.getQuantity() + " Item , Total:");

            setProductImage(order.getProductImage());
            setStatusBackground(order.getStatus());
        }

        private void setProductImage(String imageName) {
            switch (imageName) {
                case "nike_air_1":
                case "nike_air_444":
                    ivProductImage.setImageResource(R.drawable.nike_air_444);
                    break;
                default:
                    ivProductImage.setImageResource(R.drawable.placeholder_shoe);
                    break;
            }
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