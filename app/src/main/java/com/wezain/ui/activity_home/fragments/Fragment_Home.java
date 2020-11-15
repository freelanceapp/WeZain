package com.wezain.ui.activity_home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wezain.R;
import com.wezain.adapters.DataAdapter;
import com.wezain.adapters.SliderAdapter;
import com.wezain.databinding.FragmentHomeBinding;

import com.wezain.models.BankDataModel;
import com.wezain.ui.activity_home.HomeActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Fragment_Home extends Fragment {
    private FragmentHomeBinding binding;
    private double lat = 0.0, lng = 0.0;
    private HomeActivity activity;
    private SliderAdapter sliderAdapter;
    private DataAdapter auctionAdapter;
    private int current_page = 0, NUM_PAGES;

    public static Fragment_Home newInstance(double lat, double lng) {
        Bundle bundle = new Bundle();
        bundle.putDouble("lat", lat);
        bundle.putDouble("lng", lng);
        Fragment_Home fragment_home = new Fragment_Home();
        fragment_home.setArguments(bundle);
        return fragment_home;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        initView();
        change_slide_image();
        return binding.getRoot();
    }

    private void initView() {

        activity = (HomeActivity) getActivity();
        Bundle bundle = getArguments();
        auctionAdapter = new DataAdapter(new ArrayList<BankDataModel.BankModel>(), activity);
        binding.recViewAccessories.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.recViewAccessories.setAdapter(auctionAdapter);
        binding.recViewFavoriteOffers.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.recViewFavoriteOffers.setAdapter(auctionAdapter);
        SliderAdapter sliderAdapter = new SliderAdapter(new ArrayList<>(), activity);
        binding.tab.setupWithViewPager(binding.pager);
        binding.pager.setAdapter(sliderAdapter);

        if (bundle != null) {
            lat = bundle.getDouble("lat");
            lng = bundle.getDouble("lng");
        }

    }

    private void change_slide_image() {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (current_page == NUM_PAGES) {
                    current_page = 0;
                }
                binding.pager.setCurrentItem(current_page++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
    }

}
