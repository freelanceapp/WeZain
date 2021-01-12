package com.wezain.ui.activity_home.fragments;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wezain.R;
import com.wezain.adapters.MainCategoriesAdapter;
import com.wezain.databinding.FragmentCategoriesBinding;
import com.wezain.models.CategoryDataModel;
import com.wezain.models.MainDepartmentModel;
import com.wezain.models.UserModel;
import com.wezain.preferences.Preferences;
import com.wezain.remote.Api;
import com.wezain.tags.Tags;
import com.wezain.ui.activity_home.HomeActivity;

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

    private List<CategoryDataModel> subCategoryModelList;
    private List<MainDepartmentModel> categoryModelList;
    private MainCategoriesAdapter adapter;
    private String lang,country;
    private Preferences preferences;
    private UserModel userModel;
    private int selectedPos=0;


    public static Fragment_Categories newInstance(int selectedPos){
        Bundle bundle = new Bundle();
        bundle.putInt("pos",selectedPos);
        Fragment_Categories fragment_categories = new Fragment_Categories();
        fragment_categories.setArguments(bundle);
        return fragment_categories;
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
        subCategoryModelList=new ArrayList<>();
        activity = (HomeActivity) getActivity();

        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        country = Paper.book().read("country", "eg");
        Bundle bundle = getArguments();
        if (bundle!=null){
            selectedPos = bundle.getInt("pos");
        }

        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.progBarMainCategory.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recViewMainCategory.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false));
        adapter = new MainCategoriesAdapter(categoryModelList,activity,this,selectedPos);
        binding.recViewMainCategory.setAdapter(adapter);
        getCategory();

    }


    private void getCategory()
    {
        Api.getService(Tags.base_url)
                .getCategory(lang, country)
                .enqueue(new Callback<CategoryDataModel>() {
                    @Override
                    public void onResponse(Call<CategoryDataModel> call, Response<CategoryDataModel> response) {
                        binding.progBarMainCategory.setVisibility(View.GONE);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getData() != null) {
                                if (response.body().getData().size()>0){
                                    categoryModelList.clear();
                                    categoryModelList.addAll(response.body().getData());
                                    MainDepartmentModel model = categoryModelList.get(0);
                                    model.setSelected(true);
                                    categoryModelList.set(0,model);
                                    adapter.notifyDataSetChanged();
                                    binding.tvNoDataMainCategory.setVisibility(View.GONE);

                                }else {
                                    binding.tvNoDataMainCategory.setVisibility(View.VISIBLE);

                                }
                            }else {
                                binding.tvNoDataMainCategory.setVisibility(View.VISIBLE);
                            }


                        } else {
                            binding.progBarMainCategory.setVisibility(View.GONE);

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
                    public void onFailure(Call<CategoryDataModel> call, Throwable t) {
                        try {
                            binding.progBarMainCategory.setVisibility(View.GONE);
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


    public void updateSelectedPos(int selectedPos) {
        if (adapter!=null){
            this.selectedPos = selectedPos;
            adapter.updateSelectedPos(selectedPos);
        }
    }
}
