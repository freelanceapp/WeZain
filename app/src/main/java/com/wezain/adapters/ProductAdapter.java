package com.wezain.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wezain.R;
import com.wezain.databinding.ProductRowBinding;
import com.wezain.models.ProductModel;
import com.wezain.ui.activity_home.fragments.Fragment_Home;
import com.wezain.ui.activity_products.ProductsActivity;

import java.util.List;

import io.paperdb.Paper;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyHolder> {

    private List<ProductModel> list;
    private Context context;
    private String country;
    private ProductsActivity activity;


    public ProductAdapter(List<ProductModel> list, Context context,String country) {
        this.list = list;
        this.context = context;
        this.country = country;
        activity = (ProductsActivity) context;


    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductRowBinding bankRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.product_row, parent, false);
        return new MyHolder(bankRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        ProductModel model = list.get(position);
        holder.binding.setModel(model);
        holder.binding.setCountry(country);
        holder.binding.tvOldPrice.setPaintFlags(holder.binding.tvOldPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        holder.itemView.setOnClickListener(view -> {
            ProductModel model2 = list.get(holder.getAdapterPosition());
            activity.setItemData(model2);
        });

        holder.binding.checkbox.setOnClickListener(view -> {
            ProductModel model2 = list.get(holder.getAdapterPosition());
            if (holder.binding.checkbox.isChecked()){
                model2.setProduct_likes(new ProductModel.ProductLikes());


            }else {
                model2.setProduct_likes(null);


            }
            list.set(holder.getAdapterPosition(),model2);
            notifyItemChanged(holder.getAdapterPosition());
            activity.add_remove_favorite(model2,holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private ProductRowBinding binding;

        public MyHolder(ProductRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }


    }
}
