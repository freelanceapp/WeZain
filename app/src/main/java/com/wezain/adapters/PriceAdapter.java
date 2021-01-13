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
import com.wezain.databinding.PriceRowBinding;
import com.wezain.models.MainDepartmentModel;
import com.wezain.models.ProductModel;
import com.wezain.ui.activity_home.fragments.Fragment_Categories;

import java.util.List;

public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.MyHolder> {

    private List<ProductModel.Product_Prices> list;
    private Context context;
    private int selectedPos = 0;
    private int oldPos = selectedPos;
    private String country;

    public PriceAdapter(List<ProductModel.Product_Prices> list, Context context, String country) {
        this.list = list;
        this.context = context;
        this.country = country;




    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PriceRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.price_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        holder.binding.setModel(list.get(position));
        holder.binding.setCountry(country);
        ProductModel.Product_Prices model = list.get(position);

        if (model.isSelected()){
            holder.binding.fl.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
            holder.binding.fl.setCardElevation(5f);
            holder.binding.tv1.setBackgroundResource(R.color.white);
            holder.binding.tv2.setBackgroundResource(R.color.white);
            holder.binding.tv3.setBackgroundResource(R.color.white);

        }else {
            holder.binding.fl.setCardBackgroundColor(ContextCompat.getColor(context,R.color.transparent));
            holder.binding.fl.setCardElevation(0f);
            holder.binding.tv1.setBackgroundResource(R.color.transparent);
            holder.binding.tv2.setBackgroundResource(R.color.transparent);
            holder.binding.tv3.setBackgroundResource(R.color.transparent);

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
        private PriceRowBinding binding;

        public MyHolder(PriceRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }


    }

    private void updateSelectedPos(int selectedPos){
        this.selectedPos = selectedPos;

        if (oldPos!=this.selectedPos){
            ProductModel.Product_Prices model2 = list.get(oldPos);
            model2.setSelected(false);
            list.set(oldPos,model2);
            notifyItemChanged(oldPos);
        }


        ProductModel.Product_Prices model1 = list.get(this.selectedPos);
        model1.setSelected(true);
        list.set(selectedPos,model1);
        notifyItemChanged(this.selectedPos);

        oldPos = this.selectedPos;
    }
}
