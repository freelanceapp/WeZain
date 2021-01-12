package com.wezain.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wezain.R;
import com.wezain.databinding.MainCategoryRow2Binding;
import com.wezain.databinding.MainCategoryRowBinding;
import com.wezain.models.MainDepartmentModel;
import com.wezain.ui.activity_home.fragments.Fragment_Categories;
import com.wezain.ui.activity_home.fragments.Fragment_Home;

import java.util.List;

public class MainCategoriesAdapter extends RecyclerView.Adapter<MainCategoriesAdapter.MyHolder> {

    private List<MainDepartmentModel> list;
    private Context context;
    private int selectedPos = 0;
    private int oldPos = selectedPos;
    private Fragment_Categories fragment_categories;

    public MainCategoriesAdapter(List<MainDepartmentModel> list, Context context, Fragment_Categories fragment_categories,int selectedPos) {
        this.list = list;
        this.context = context;
        this.fragment_categories = fragment_categories;
        this.selectedPos = selectedPos;



    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MainCategoryRow2Binding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.main_category_row2, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        holder.binding.setModel(list.get(position));
        MainDepartmentModel mainDepartmentModel = list.get(position);

        if (mainDepartmentModel.isSelected()){
            holder.binding.cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.color1));
            holder.binding.tvTitle.setTextColor(ContextCompat.getColor(context,R.color.white));
        }else {
            holder.binding.cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.gray5));
            holder.binding.tvTitle.setTextColor(ContextCompat.getColor(context,R.color.gray9));

        }

        holder.itemView.setOnClickListener(view -> {
            selectedPos = holder.getAdapterPosition();

            updateSelectedPos(selectedPos);





        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private MainCategoryRow2Binding binding;

        public MyHolder(MainCategoryRow2Binding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }


    }

    public void updateSelectedPos(int selectedPos){
        this.selectedPos = selectedPos;

        if (oldPos!=this.selectedPos){
            MainDepartmentModel model2 = list.get(oldPos);
            model2.setSelected(false);
            list.set(oldPos,model2);
            notifyItemChanged(oldPos);
        }


        MainDepartmentModel model1 = list.get(this.selectedPos);
        model1.setSelected(true);
        list.set(selectedPos,model1);
        notifyItemChanged(this.selectedPos);

        oldPos = this.selectedPos;
    }
}
