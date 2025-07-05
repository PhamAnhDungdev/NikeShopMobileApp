package com.example.nikeshop.Adapter;

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

import com.example.nikeshop.Models.ProductFavourite;
import com.example.nikeshop.Models.ProductFavourite;
import com.example.nikeshop.R;

import java.util.List;

public class FavouriteProductAdapter extends RecyclerView.Adapter<FavouriteProductAdapter.FavouriteViewHolder> {

    private List<ProductFavourite> productList;
    private Context context;

    public FavouriteProductAdapter(List<ProductFavourite> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favourite_product, parent, false);
        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {
        ProductFavourite product = productList.get(position);

        holder.tvProductName.setText(product.getName());
        holder.tvProductType.setText(product.getType());
        holder.tvProductPrice.setText(product.getPrice());
        holder.imgProduct.setImageResource(product.getImageResId());

        holder.btnAddToCart.setOnClickListener(v -> {
            Toast.makeText(context, product.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
            // TODO: Add your cart logic here
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class FavouriteViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName, tvProductType, tvProductPrice;
        ImageButton btnAddToCart;

        public FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductType = itemView.findViewById(R.id.tvProductType);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}
