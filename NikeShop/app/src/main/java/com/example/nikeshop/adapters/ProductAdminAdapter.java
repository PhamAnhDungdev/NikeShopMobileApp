package com.example.nikeshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nikeshop.R;
import com.example.nikeshop.data.local.entity.Product;

import java.util.List;

public class ProductAdminAdapter extends RecyclerView.Adapter<ProductAdminAdapter.ProductViewHolder> {

    public interface OnProductActionListener {
        void onEdit(Product product);
        void onDelete(Product product);
    }

    private final List<Product> productList;
    private final Context context;
    private final OnProductActionListener listener;

    public ProductAdminAdapter(Context context, List<Product> productList, OnProductActionListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_admin, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(String.format("%,.0f VND", product.getPrice()));
        holder.tvStock.setText("Kho: " + product.getStockQuantity());

        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.ic_image_placeholder)
                .into(holder.imgProduct);

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(product));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvPrice, tvStock;
        ImageButton btnEdit, btnDelete;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_product_admin);
            tvName = itemView.findViewById(R.id.tv_product_name_admin);
            tvPrice = itemView.findViewById(R.id.tv_product_price_admin);
            tvStock = itemView.findViewById(R.id.tv_product_stock);
            btnEdit = itemView.findViewById(R.id.btn_edit_product);
            btnDelete = itemView.findViewById(R.id.btn_delete_product);
        }
    }
}
