package com.example.nikeshop.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nikeshop.R;
import com.example.nikeshop.data.local.entity.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> products;
    private final Context context;
    private final OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public ProductAdapter(Context context, List<Product> products, OnProductClickListener listener) {
        this.context = context;
        this.products = products;
        this.listener = listener;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_card, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText("$" + product.getPrice());
        holder.tvDesc.setText(product.getDescription());
        // Load ảnh động cho từng sản phẩm
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
          
            try {
                Class.forName("com.bumptech.glide.Glide");
                com.bumptech.glide.Glide.with(context).load(product.getImageUrl()).placeholder(R.drawable.shoes1).into(holder.imgProduct);
            } catch (ClassNotFoundException e) {
                // Nếu chưa có Glide, fallback về ảnh mặc định
                holder.imgProduct.setImageResource(R.drawable.shoes1);
            }
        } else {
            // Nếu không có imageUrl, dùng ảnh mặc định
            holder.imgProduct.setImageResource(R.drawable.shoes1);
        }
        holder.itemView.setOnClickListener(v -> listener.onProductClick(product));
    }

    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvPrice, tvDesc;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_product);
            tvName = itemView.findViewById(R.id.tv_product_name);
            tvPrice = itemView.findViewById(R.id.tv_product_price);
            tvDesc = itemView.findViewById(R.id.tv_product_desc);
        }
    }
}
