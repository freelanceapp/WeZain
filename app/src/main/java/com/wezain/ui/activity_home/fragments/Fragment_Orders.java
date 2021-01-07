package com.wezain.ui.activity_home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wezain.R;
import com.wezain.adapters.BrandAdapter;
import com.wezain.adapters.OfferAdapter;
import com.wezain.databinding.FragmentOrdersBinding;
import com.wezain.models.BankDataModel;
import com.wezain.ui.activity_home.HomeActivity;

import java.util.ArrayList;

public class Fragment_Orders extends Fragment {
    private FragmentOrdersBinding binding;
    private HomeActivity activity;
    private BrandAdapter auctionAdapter;


    public static Fragment_Orders newInstance(){
        return new Fragment_Orders();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();

//        auctionAdapter = new BrandAdapter( new ArrayList<BankDataModel.BankModel>(),activity);
//        binding.recViewOrders.setLayoutManager(new GridLayoutManager(activity,3));
//        binding.recViewOrders.setAdapter(auctionAdapter);
    }
}
