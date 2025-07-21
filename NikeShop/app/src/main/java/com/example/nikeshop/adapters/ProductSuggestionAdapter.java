package com.example.nikeshop.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nikeshop.R;
import com.example.nikeshop.data.local.entity.Product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductSuggestionAdapter extends RecyclerView.Adapter<ProductSuggestionAdapter.SuggestionViewHolder> {

    private List<Product> products = new ArrayList<>();
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public ProductSuggestionAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<Product> newList) {
        // Loại bỏ trùng sản phẩm theo id
        Set<Integer> seenIds = new HashSet<>();
        List<Product> uniqueList = new ArrayList<>();

        for (Product p : newList) {
            if (seenIds.add(p.getId())) { // chỉ thêm nếu id chưa tồn tại
                uniqueList.add(p);
            }
        }

        this.products = uniqueList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public SuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_suggestion, parent, false);
        return new SuggestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product, listener);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class SuggestionViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView descriptionText;

        public SuggestionViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.tvProductName);
            descriptionText = itemView.findViewById(R.id.tvProductDesc);
        }

        public void bind(Product product, OnItemClickListener listener) {
            nameText.setText(product.getName());
            descriptionText.setText(product.getDescription());
            itemView.setOnClickListener(v -> listener.onItemClick(product));
        }
    }
}
