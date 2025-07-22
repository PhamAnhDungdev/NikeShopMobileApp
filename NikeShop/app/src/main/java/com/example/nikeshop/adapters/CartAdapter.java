
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
import com.example.nikeshop.R;
import com.example.nikeshop.data.local.model.CartWithProduct;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartWithProduct> cartList;
    private Context context;
    private OnQuantityChangeListener quantityChangeListener;

    public interface OnQuantityChangeListener {
        void onQuantityChanged(int position, int newQuantity, CartWithProduct cart);
    }

    public void setOnQuantityChangeListener(OnQuantityChangeListener listener) {
        this.quantityChangeListener = listener;
    }

    public CartAdapter(Context context, List<CartWithProduct> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    public void setCartList(List<CartWithProduct> cartList) {
        this.cartList = cartList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartWithProduct cart = cartList.get(position);
        holder.tvProductName.setText(cart.productName);
        holder.tvProductPrice.setText("Giá: $" + cart.productPrice);
        // Load ảnh sản phẩm nếu có
        if (cart.productImageUrl != null && !cart.productImageUrl.isEmpty()) {
            Glide.with(context).load(cart.productImageUrl).into(holder.imgProduct);
        }
        // Số lượng
        TextView tvQuantity = holder.itemView.findViewById(R.id.tv_quantity);
        tvQuantity.setText(String.valueOf(cart.quantity));
        TextView btnMinus = holder.itemView.findViewById(R.id.btn_minus);
        TextView btnPlus = holder.itemView.findViewById(R.id.btn_plus);
        ImageView btnDelete = holder.itemView.findViewById(R.id.btn_delete);
        btnMinus.setOnClickListener(v -> {
            int newQuantity = cart.quantity > 1 ? cart.quantity - 1 : 1;
            if (newQuantity != cart.quantity && quantityChangeListener != null) {
                quantityChangeListener.onQuantityChanged(position, newQuantity, cart);
            }
        });
        btnPlus.setOnClickListener(v -> {
            int newQuantity = cart.quantity + 1;
            if (quantityChangeListener != null) {
                quantityChangeListener.onQuantityChanged(position, newQuantity, cart);
            }
        });
        btnDelete.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClicked(position, cart);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList != null ? cartList.size() : 0;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductPrice;
        ImageView imgProduct;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tv_cart_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_cart_product_price);
            imgProduct = itemView.findViewById(R.id.img_cart_product);
        }
    }
        public interface OnDeleteClickListener {
        void onDeleteClicked(int position, CartWithProduct cart);
    }
    private OnDeleteClickListener deleteClickListener;
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }
}
