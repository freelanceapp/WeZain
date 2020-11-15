package com.wezain.ui.activity_home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.doctory_client.R;
import com.doctory_client.databinding.FragmentMedicineBinding;

public class Fragment_Medicine extends Fragment {
    private FragmentMedicineBinding binding;

    public static Fragment_Medicine newInstance(){
        return new Fragment_Medicine();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_medicine,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {

    }
}
