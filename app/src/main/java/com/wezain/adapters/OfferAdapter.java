package com.wezain.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wezain.R;
import com.wezain.databinding.OfferRowBinding;
import com.wezain.databinding.ProductRowBinding;
import com.wezain.models.BankDataModel;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.MyHolder> {

    private List<BankDataModel.BankModel> bankDataModelList;
    private Context context;

    public OfferAdapter(List<BankDataModel.BankModel> bankDataModelList, Context context) {
        this.bankDataModelList = bankDataModelList;
        this.context = context;


    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OfferRowBinding bankRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.offer_row, parent, false);
        return new MyHolder(bankRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

     //   BankDataModel.BankModel bankModel = bankDataModelList.get(position);

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private OfferRowBinding bankRowBinding;

        public MyHolder(OfferRowBinding bankRowBinding) {
            super(bankRowBinding.getRoot());
            this.bankRowBinding = bankRowBinding;


        }


    }
}
