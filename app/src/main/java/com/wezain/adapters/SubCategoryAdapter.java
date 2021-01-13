package com.wezain.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wezain.R;
import com.wezain.databinding.OfferRowBinding;
import com.wezain.databinding.SubCategoryRowBinding;
import com.wezain.models.BankDataModel;
import com.wezain.models.SubDepartmentModel;
import com.wezain.ui.activity_home.fragments.Fragment_Categories;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyHolder> {

    private List<SubDepartmentModel> list;
    private Context context;
    private Fragment_Categories fragment_categories;
    public SubCategoryAdapter(List<SubDepartmentModel> list, Context context,Fragment_Categories fragment_categories) {
        this.fragment_categories = fragment_categories;
        this.context = context;
        this.list = list;


    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SubCategoryRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.sub_category_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        SubDepartmentModel model = list.get(position);
        holder.binding.setModel(model);
        holder.itemView.setOnClickListener(view -> {
            SubDepartmentModel model2 = list.get(holder.getAdapterPosition());
            fragment_categories.setItemData(model2);

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private SubCategoryRowBinding binding;

        public MyHolder(SubCategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }


    }
}
