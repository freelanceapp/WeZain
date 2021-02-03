package com.wezain.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wezain.R;
import com.wezain.databinding.ProductRow3Binding;
import com.wezain.models.OrderModel;
import java.util.List;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.MyHolder> {

    private List<OrderModel.OrderDetailsProduct> list;
    private Context context;
    private String country;
    private OrderModel orderModel;

    public OrderProductAdapter(List<OrderModel.OrderDetailsProduct> list, Context context,String country,OrderModel orderModel) {
        this.list = list;
        this.context = context;
        this.country = country;
        this.orderModel = orderModel;


    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductRow3Binding bankRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.product_row3, parent, false);
        return new MyHolder(bankRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        Log.e("mmmmmmmmmmmm",orderModel.getBill_currency()+"-----------");
        OrderModel.OrderDetailsProduct model = list.get(position);
        holder.binding.setModel(model);
        holder.binding.setCountry(country);
        holder.binding.setOrdermodel(orderModel);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private ProductRow3Binding binding;

        public MyHolder(ProductRow3Binding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }


    }
}
