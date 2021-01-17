package com.wezain.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wezain.R;
import com.wezain.databinding.ProductFavoriteRowBinding;
import com.wezain.databinding.ProductSearchRowBinding;
import com.wezain.models.ProductModel;
import com.wezain.ui.activity_favorite.FavoriteActivity;
import com.wezain.ui.activity_search.SearchActivity;

import java.util.List;

public class SearchProductAdapter extends RecyclerView.Adapter<SearchProductAdapter.MyHolder> {

    private List<ProductModel> list;
    private Context context;
    private SearchActivity activity;
    private String country;

    public SearchProductAdapter(List<ProductModel> list, Context context, String country) {
        this.list = list;
        this.context = context;
        activity = (SearchActivity) context;
        this.country = country;


    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductSearchRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.product_search_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        ProductModel model = list.get(position);
        holder.binding.setModel(model);
        holder.binding.setCountry(country);
        holder.itemView.setOnClickListener(view -> {
            ProductModel model2 = list.get(position);
            activity.setItemData(model2);

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private ProductSearchRowBinding binding;

        public MyHolder(ProductSearchRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }


    }
}
