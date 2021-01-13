package com.wezain.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.wezain.R;
import com.wezain.databinding.MainCategoryRowBinding;
import com.wezain.models.MainDepartmentModel;
import com.wezain.ui.activity_home.fragments.Fragment_Home;

import java.util.List;

public class HomeCategoriesAdapter extends RecyclerView.Adapter<HomeCategoriesAdapter.MyHolder> {

    private List<MainDepartmentModel> list;
    private Context context;
    private Fragment_Home fragment_home;

    public HomeCategoriesAdapter(List<MainDepartmentModel> list, Context context, Fragment_Home fragment_home) {
        this.list = list;
        this.context = context;
        this.fragment_home = fragment_home;


    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MainCategoryRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.main_category_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        holder.binding.setModel(list.get(position));

        holder.itemView.setOnClickListener(view -> {
            fragment_home.setCategorySelected(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private MainCategoryRowBinding binding;

        public MyHolder(MainCategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }


    }
}
