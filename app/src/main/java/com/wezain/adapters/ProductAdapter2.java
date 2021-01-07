package com.wezain.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wezain.R;
import com.wezain.databinding.ProductRow2Binding;
import com.wezain.models.ProductModel;

import java.util.List;

public class ProductAdapter2 extends RecyclerView.Adapter<ProductAdapter2.MyHolder> {

    private List<ProductModel> list;
    private Context context;

    public ProductAdapter2(List<ProductModel> list, Context context) {
        this.list = list;
        this.context = context;


    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductRow2Binding bankRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.product_row2, parent, false);
        return new MyHolder(bankRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        ProductModel model = list.get(position);
        holder.binding.setModel(model);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private ProductRow2Binding binding;

        public MyHolder(ProductRow2Binding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }


    }
}
