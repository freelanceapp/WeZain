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

import java.util.List;

import io.paperdb.Paper;

public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.MyHolder> {

    private List<ProductModel> list;
    private Context context;
    private Fragment_Home fragment_home;
    private String country;
    private String type;

    public HomeProductAdapter(List<ProductModel> list, Context context, Fragment_Home fragment_home,String type,String country) {
        this.list = list;
        this.context = context;
        this.fragment_home = fragment_home;
        Paper.init(context);
        this.country = country;
        this.type = type;


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
        holder.binding.checkbox.setOnClickListener(view -> {
            ProductModel model2 = list.get(holder.getAdapterPosition());
            if (holder.binding.checkbox.isChecked()){
                model2.setProduct_likes(new ProductModel.ProductLikes());


            }else {
                model2.setProduct_likes(null);


            }
            list.set(holder.getAdapterPosition(),model2);
            notifyItemChanged(holder.getAdapterPosition());
            fragment_home.add_remove_favorite(model2,holder.getAdapterPosition(),type);
        });

        holder.itemView.setOnClickListener(view -> {
            ProductModel model2 = list.get(holder.getAdapterPosition());
            fragment_home.setItemData(model2);
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

    public void updateCountry(String country){
        this.country = country;
    }
}
