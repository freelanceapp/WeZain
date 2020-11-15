package com.wezain.ui.activity_home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.wezain.R;
import com.wezain.adapters.SliderAdapter;
import com.wezain.databinding.FragmentHomeBinding;

import com.wezain.ui.activity_home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Home extends Fragment {
    private FragmentHomeBinding binding;
    private double lat=0.0,lng=0.0;
    private HomeActivity activity;
    private SliderAdapter sliderAdapter;


    public static Fragment_Home newInstance(double lat,double lng){
        Bundle bundle = new Bundle();
        bundle.putDouble("lat",lat);
        bundle.putDouble("lng",lng);
        Fragment_Home fragment_home = new Fragment_Home();
        fragment_home.setArguments(bundle);
        return fragment_home;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        Bundle bundle = getArguments();
        if (bundle!=null){
            lat = bundle.getDouble("lat");
            lng = bundle.getDouble("lng");
        }

    }


}