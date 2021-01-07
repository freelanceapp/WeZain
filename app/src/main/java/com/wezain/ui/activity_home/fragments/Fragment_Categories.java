package com.wezain.ui.activity_home.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wezain.R;
import com.wezain.adapters.BrandAdapter;
import com.wezain.adapters.CategoriesAdapter;
import com.wezain.adapters.HomeCategoriesAdapter;
import com.wezain.databinding.FragmentCategoriesBinding;
import com.wezain.models.BankDataModel;
import com.wezain.models.CategoryDataModel;
import com.wezain.models.DepartmentModel;
import com.wezain.models.MainCategoryDataModel;
import com.wezain.remote.Api;
import com.wezain.tags.Tags;
import com.wezain.ui.activity_home.HomeActivity;
import com.wezain.ui.activity_products.ProductsActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Categories extends Fragment {
    private FragmentCategoriesBinding binding;
    private HomeActivity activity;
    private BrandAdapter brandAdapter;
    private CategoriesAdapter categoriesAdapter;
    private List<CategoryDataModel> categoryModelList;
    private List<DepartmentModel> departmentModels;
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
        binding.recViewdep.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL,false));
        binding.progBar.setVisibility(View.GONE);
        brandAdapter = new BrandAdapter( departmentModels,activity,this);
        categoriesAdapter=new CategoriesAdapter(categoryModelList,activity,this);
        binding.recView.setLayoutManager(new GridLayoutManager(activity,3));
        binding.recView.setAdapter(brandAdapter);
        binding.recViewdep.setAdapter(categoriesAdapter);
        getCategory();
    }
    private void getCategory()
    {
        Api.getService(Tags.base_url)
                .getCategorywithsub(lang, country)
                .enqueue(new Callback<MainCategoryDataModel>() {
                    @Override
                    public void onResponse(Call<MainCategoryDataModel> call, Response<MainCategoryDataModel> response) {
                        binding.progBardep.setVisibility(View.GONE);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getData() != null) {
                                if (response.body().getData().size()>0){
                                    categoryModelList.clear();
                                    categoryModelList.addAll(response.body().getData());
                                    categoriesAdapter.notifyDataSetChanged();
                                    binding.tvNoDatadep.setVisibility(View.GONE);

                                }else {
                                    binding.tvNoDatadep.setVisibility(View.VISIBLE);

                                }
                            }else {
                                binding.tvNoDatadep.setVisibility(View.VISIBLE);
                            }


                        } else {
                            binding.progBardep.setVisibility(View.GONE);

                            switch (response.code()) {
                                case 500:
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                                    break;

                                default:
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            try {
                                Log.e("error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<MainCategoryDataModel> call, Throwable t) {
                        try {
                            binding.progBardep.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });
    }

    public void setitem(List<DepartmentModel> data) {
        departmentModels.clear();

        departmentModels.addAll(data);
        if(data.size()==0){
            binding.tvNoData.setVisibility(View.VISIBLE);
        }
        else {
            binding.tvNoData.setVisibility(View.GONE);
        }
        brandAdapter.notifyDataSetChanged();
    }
    public void setChildItemData(DepartmentModel departmentModel) {
        Intent intent = new Intent(activity, ProductsActivity.class);
        intent.putExtra("data1", departmentModel);


        startActivity(intent);
    }
}
