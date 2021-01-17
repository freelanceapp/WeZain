package com.wezain.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.wezain.R;
import com.wezain.databinding.ProductFavoriteRowBinding;
import com.wezain.models.ProductModel;
import com.wezain.ui.activity_favorite.FavoriteActivity;

import java.util.List;

public class FavoriteProductAdapter extends RecyclerView.Adapter<FavoriteProductAdapter.MyHolder> {

    private List<ProductModel> list;
    private Context context;
    private FavoriteActivity activity;
    private String country;

    public FavoriteProductAdapter(List<ProductModel> list, Context context,String country) {
        this.list = list;
        this.context = context;
        activity = (FavoriteActivity) context;
        this.country = country;


    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductFavoriteRowBinding bankRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.product_favorite_row, parent, false);
        return new MyHolder(bankRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        ProductModel model = list.get(position);
        holder.binding.setModel(model);
        holder.binding.setCountry(country);
        holder.binding.imageFavorite.setOnClickListener(view -> {
            ProductModel model2 = list.get(holder.getAdapterPosition());
            activity.removeFavorite(model2,holder.getAdapterPosition());

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private ProductFavoriteRowBinding binding;

        public MyHolder(ProductFavoriteRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }


    }
}
