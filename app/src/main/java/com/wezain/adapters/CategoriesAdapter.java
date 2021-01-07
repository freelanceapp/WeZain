package com.wezain.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wezain.R;
import com.wezain.databinding.CategoryRowBinding;
import com.wezain.databinding.MainCategoryRowBinding;
import com.wezain.models.CategoryDataModel;
import com.wezain.models.DepartmentModel;
import com.wezain.ui.activity_home.fragments.Fragment_Categories;
import com.wezain.ui.activity_home.fragments.Fragment_Home;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyHolder> {

    private List<CategoryDataModel> list;
    private Context context;
    private int i = 0;
    private Fragment_Categories fragment_categories;

    public CategoriesAdapter(List<CategoryDataModel> list, Context context, Fragment_Categories fragment_categories) {
        this.list = list;
        this.context = context;
        this.fragment_categories = fragment_categories;


    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.category_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        holder.binding.setModel(list.get(position));

holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        i=position;
        notifyDataSetChanged();
    }
});
if(i==position){
    fragment_categories.setitem(list.get(position).getSub_departments());
holder.binding.tvName.setTextColor(context.getResources().getColor(R.color.white));
    holder.binding.ll.setBackground(context.getResources().getDrawable(R.drawable.rounded_red));
}
else {
    holder.binding.ll.setBackground(context.getResources().getDrawable(R.drawable.rounded_gray));
    holder.binding.tvName.setTextColor(context.getResources().getColor(R.color.black));

}
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private CategoryRowBinding binding;

        public MyHolder(CategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }


    }
}
