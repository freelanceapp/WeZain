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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wezain.R;
import com.wezain.databinding.FragmentCategoriesBinding;
import com.wezain.models.CategoryDataModel;
import com.wezain.models.MainDepartmentModel;
import com.wezain.ui.activity_home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class Fragment_Categories extends Fragment {
    private FragmentCategoriesBinding binding;
    private HomeActivity activity;

    private List<CategoryDataModel> categoryModelList;
    private List<MainDepartmentModel> departmentModels;
    private String lang,country;

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
        categoryModelList=new ArrayList<>();
        departmentModels=new ArrayList<>();
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        country = Paper.book().read("country", "eg");
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.progBar.setVisibility(View.GONE);

    }



}
