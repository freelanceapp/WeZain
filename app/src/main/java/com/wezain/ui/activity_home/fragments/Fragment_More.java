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
import com.doctory_client.databinding.FragmentMoreBinding;
import com.doctory_client.ui.activity_home.HomeActivity;

import io.paperdb.Paper;

public class Fragment_More extends Fragment {
    private FragmentMoreBinding binding;
    private String lang;
    private HomeActivity activity;

    public static Fragment_More newInstance(){
        return new Fragment_More();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
    }
}
