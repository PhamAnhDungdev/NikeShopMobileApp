package com.example.nikeshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nikeshop.R;
import com.example.nikeshop.data.local.entity.Order;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;
    private OnOrderClickListener listener;

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public OrdersAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    public void setOnOrderClickListener(OnOrderClickListener listener) {
        this.listener = listener;
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onOrderClick(orderList.get(position));
                        }
                    }
                }
            });
        }

        public void bind(Order order) {
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

            // Hiển thị ID đơn hàng
            tvOrderNumber.setText("Order #" + order.getId());

            // Hiển thị ngày đặt hàng
            tvPlacedDate.setText("Placed on " + order.getOrderDate());

            // Dữ liệu không có => bỏ dòng này
            tvPaidDate.setVisibility(View.GONE);

            // Không có tên sản phẩm, category => dùng placeholder hoặc ẩn
            tvProductName.setText("Nike Product");
            tvProductCategory.setText("Category");
            tvProductPrice.setText(currencyFormat.format(order.getTotalPrice())); // giả định 1 sản phẩm duy nhất

            tvTotalPrice.setText(currencyFormat.format(order.getTotalPrice()));
            tvProductQuantity.setText("X1"); // chưa có quantity => giả định 1
            tvOrderStatus.setText(order.getStatus());
            tvItemCount.setText("1 Item , Total:");

            // Ảnh sản phẩm không có => placeholder
            ivProductImage.setImageResource(R.drawable.placeholder_shoe);

            setStatusBackground(order.getStatus());
        }


        private void setProductImage(String imageName) {
            switch (imageName) {
                case "nike_air_1":
                    ivProductImage.setImageResource(R.drawable.nike_air_444);
                    break;
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