package com.wezain.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wezain.R;
import com.wezain.databinding.AddressRowBinding;
import com.wezain.models.AddressModel;
import com.wezain.ui.activity_select_address.SelectAddressActivity;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyHolder> {

    private List<AddressModel> list;
    private Context context;
    private SelectAddressActivity activity;

    public AddressAdapter(List<AddressModel> list, Context context) {
        this.list = list;
        this.context = context;
        activity = (SelectAddressActivity) context;


    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AddressRowBinding bankRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.address_row, parent, false);
        return new MyHolder(bankRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        AddressModel model = list.get(position);
        holder.binding.setModel(model);
        holder.itemView.setOnClickListener(view -> {
            AddressModel model2 = list.get(holder.getAdapterPosition());
            activity.setItemSelect(model2);
        });
        holder.binding.imageUpdate.setOnClickListener(view -> {
            AddressModel model2 = list.get(holder.getAdapterPosition());
            activity.updateAddress(model2,holder.getAdapterPosition());
        });

        holder.binding.imageDelete.setOnClickListener(view -> {
            AddressModel model2 = list.get(holder.getAdapterPosition());
            activity.deleteAddress(model2,holder.getAdapterPosition());
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private AddressRowBinding binding;

        public MyHolder(AddressRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }


    }
}
