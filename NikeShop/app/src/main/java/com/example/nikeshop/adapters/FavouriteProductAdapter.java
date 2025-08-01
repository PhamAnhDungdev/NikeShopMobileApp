package com.example.nikeshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nikeshop.R;
import com.example.nikeshop.data.local.entity.Product;
import com.example.nikeshop.ui.ViewModels.CartViewModel;
import com.example.nikeshop.utils.Util;

import java.util.List;

public class FavouriteProductAdapter extends RecyclerView.Adapter<FavouriteProductAdapter.FavouriteViewHolder> {

    private List<Product> productList;
    private final Context context;
    private final CartViewModel cartViewModel;

    public interface OnDeleteClickListener {
        void onDeleteClick(int position, Product product);
    }

    private OnDeleteClickListener onDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    public FavouriteProductAdapter(List<Product> productList, Context context, CartViewModel cartViewModel) {
        this.productList = productList;
        this.context = context;
        this.cartViewModel = cartViewModel;
    }

    public void setProducts(List<Product> newList) {
        this.productList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favourite_product, parent, false);
        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tvProductName.setText(product.getName());
        holder.tvProductType.setText(product.getDescription());
        holder.tvProductPrice.setText("$" + product.getPrice());

        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.shoes1)
                    .into(holder.imgProduct);
        } else {
            holder.imgProduct.setImageResource(R.drawable.shoes1);
        }

        holder.btnAddToCart.setOnClickListener(v -> {
            cartViewModel.addToCart(product.getId(), 1);
            Toast.makeText(context, product.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
        });

        holder.btnUnfavorite.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    onDeleteClickListener.onDeleteClick(pos, product);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    static class FavouriteViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvProductType, tvProductPrice;
        ImageButton btnAddToCart, btnUnfavorite;

        public FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductType = itemView.findViewById(R.id.tvProductType);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            btnUnfavorite = itemView.findViewById(R.id.btnUnfavorite);
        }
    }
}
