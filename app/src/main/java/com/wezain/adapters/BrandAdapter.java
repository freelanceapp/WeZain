package com.wezain.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wezain.R;
import com.wezain.databinding.BrandRowBinding;
import com.wezain.models.DepartmentModel;
import com.wezain.ui.activity_home.fragments.Fragment_Categories;

import java.util.List;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.MyHolder> {

    private List<DepartmentModel> departmentModelList;
    private Context context;
    private Fragment_Categories fragment_categories;

    public BrandAdapter(List<DepartmentModel> departmentModelList, Context context, Fragment_Categories fragment_categories) {
        this.departmentModelList = departmentModelList;
        this.context = context;
        this.fragment_categories = fragment_categories;

    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BrandRowBinding bankRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.brand_row, parent, false);
        return new MyHolder(bankRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        //  BankDataModel.BankModel bankModel = bankDataModelList.get(position);
        holder.bankRowBinding.setModel(departmentModelList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_categories.setChildItemData(departmentModelList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return departmentModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private BrandRowBinding bankRowBinding;

        public MyHolder(BrandRowBinding bankRowBinding) {
            super(bankRowBinding.getRoot());
            this.bankRowBinding = bankRowBinding;


        }


    }
}
