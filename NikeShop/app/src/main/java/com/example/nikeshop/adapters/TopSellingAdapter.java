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
import com.example.nikeshop.Models.TopSellingProductItem;
import com.example.nikeshop.R;


import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TopSellingAdapter extends RecyclerView.Adapter<TopSellingAdapter.TopSellingViewHolder> {

    private final Context context;
    private List<TopSellingProductItem> topSellingItems;

    public TopSellingAdapter(Context context) {
        this.context = context;
    }

    public void setProducts(List<TopSellingProductItem> products) {
        this.topSellingItems = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TopSellingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_top_selling, parent, false);
        return new TopSellingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopSellingViewHolder holder, int position) {
        TopSellingProductItem item = topSellingItems.get(position);
        holder.tvName.setText(item.name);
        holder.tvQuantity.setText("Đã bán: " + item.totalSold);
        holder.tvRevenue.setText("Doanh thu: " +
                NumberFormat.getNumberInstance(Locale.US).format(item.totalRevenue) + " VND");


        Glide.with(context)
                .load(item.imageUrl)
                .placeholder(R.drawable.placeholder_shoe)
                .into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return topSellingItems != null ? topSellingItems.size() : 0;
    }

    static class TopSellingViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvQuantity, tvRevenue;

        public TopSellingViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_product);
            tvName = itemView.findViewById(R.id.tv_product_name);
            tvQuantity = itemView.findViewById(R.id.tv_quantity_sold);
            tvRevenue = itemView.findViewById(R.id.tv_total_revenue);
        }
    }
}
