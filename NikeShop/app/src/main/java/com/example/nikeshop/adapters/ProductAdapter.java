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

    private com.example.nikeshop.ui.ViewModels.CartViewModel cartViewModel;
    private com.example.nikeshop.ui.ViewModels.WishlistViewModel wishlistViewModel;

    public void setCartViewModel(com.example.nikeshop.ui.ViewModels.CartViewModel cartViewModel) {
        this.cartViewModel = cartViewModel;
    }
    public void setWishlistViewModel(com.example.nikeshop.ui.ViewModels.WishlistViewModel wishlistViewModel) {
        this.wishlistViewModel = wishlistViewModel;
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

        // Xử lý click nút cộng
        ImageView btnAddToCart = holder.itemView.findViewById(R.id.btn_add_to_cart);
        btnAddToCart.setOnClickListener(v -> {
            android.content.SharedPreferences prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
            if (!isLoggedIn) {
                android.content.Intent intent = new android.content.Intent(context, com.example.nikeshop.ui.activities.LoginActivity.class);
                intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                if (cartViewModel != null) {
                    cartViewModel.addToCart(product.getId(), 1);
                    android.widget.Toast.makeText(context, "Đã thêm sản phẩm vào giỏ hàng!", android.widget.Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý click icon yêu thích
        ImageView btnFavourite = holder.itemView.findViewById(R.id.btn_favourite);
        btnFavourite.setOnClickListener(v -> {
            android.content.SharedPreferences prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
            if (!isLoggedIn) {
                android.content.Intent intent = new android.content.Intent(context, com.example.nikeshop.ui.activities.LoginActivity.class);
                intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                if (wishlistViewModel == null) {
                    wishlistViewModel = new androidx.lifecycle.ViewModelProvider((androidx.fragment.app.FragmentActivity) context).get(com.example.nikeshop.ui.ViewModels.WishlistViewModel.class);
                }
                wishlistViewModel.addToWishlist(product.getId(), new com.example.nikeshop.ui.ViewModels.WishlistViewModel.AddToWishlistListener() {
                    @Override
                    public void onSuccess() {
                        android.widget.Toast.makeText(context, "Đã thêm sản phẩm vào danh sách yêu thích!", android.widget.Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onExists() {
                        android.widget.Toast.makeText(context, "Sản phẩm đã có trong danh sách yêu thích!", android.widget.Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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
            // btn_favourite đã được bind trong onBindViewHolder
        }
    }
}
