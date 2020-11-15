package com.wezain.ui.activity_home.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wezain.R;
import com.wezain.adapters.BrandAdapter;
import com.wezain.databinding.FragmentCategoriesBinding;
import com.wezain.models.BankDataModel;
import com.wezain.ui.activity_home.HomeActivity;

import java.util.ArrayList;

public class Fragment_Categories extends Fragment {
    private FragmentCategoriesBinding binding;
    private HomeActivity activity;
    private BrandAdapter auctionAdapter;

    public static Fragment_Categories newInstance(){
        return new Fragment_Categories();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_categories,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.progBar.setVisibility(View.GONE);
        auctionAdapter = new BrandAdapter( new ArrayList<BankDataModel.BankModel>(),activity);
        binding.recView.setLayoutManager(new GridLayoutManager(activity,3));
        binding.recView.setAdapter(auctionAdapter);
    }
}
