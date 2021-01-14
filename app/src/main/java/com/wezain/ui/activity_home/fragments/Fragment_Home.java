package com.wezain.ui.activity_home.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.squareup.picasso.Picasso;
import com.wezain.R;
import com.wezain.adapters.HomeCategoriesAdapter;
import com.wezain.adapters.HomeProductAdapter;
import com.wezain.adapters.SliderAdapter;
import com.wezain.databinding.FragmentHomeBinding;

import com.wezain.models.CategoryDataModel;
import com.wezain.models.MainDepartmentModel;
import com.wezain.models.MainSliderImageDataModel;
import com.wezain.models.ProductModel;
import com.wezain.models.SliderDataModel;
import com.wezain.models.SliderModel;
import com.wezain.models.UserModel;
import com.wezain.preferences.Preferences;
import com.wezain.remote.Api;
import com.wezain.tags.Tags;
import com.wezain.ui.activity_home.HomeActivity;
import com.wezain.ui.activity_product_details.ProductDetailsActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Home extends Fragment {
    private FragmentHomeBinding binding;
    private HomeActivity activity;
    private SliderAdapter sliderAdapter;
    private List<SliderModel> sliderModelList;
    private HomeCategoriesAdapter homeCategoriesAdapter;
    private List<MainDepartmentModel>  categoryModelList;
    private HomeProductAdapter flashProductAdapter,newProductAdapter,recommendedProductAdapter;
    private List<ProductModel> flashProductModelList,newProductModelList,recommendedProductModelList;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private String country;

    public static Fragment_Home newInstance() {
        return new Fragment_Home();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {

        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        country = Paper.book().read("country", "not_selected");

        if (userModel==null){
            if (country.equals("not_selected")){
                country = "em";
            }
        }else {
            if (country.equals("not_selected")){
                if (userModel.getData().getPhone_code().equals("+971")){
                    country = "em";
                }else {
                    country = "eg";
                }
            }
        }

        sliderModelList = new ArrayList<>();
        categoryModelList = new ArrayList<>();
        flashProductModelList = new ArrayList<>();
        newProductModelList = new ArrayList<>();
        recommendedProductModelList = new ArrayList<>();


        sliderAdapter = new SliderAdapter(sliderModelList, activity);
        binding.tab.setupWithViewPager(binding.pager);
        binding.pager.setAdapter(sliderAdapter);

        homeCategoriesAdapter = new HomeCategoriesAdapter(categoryModelList,activity,this);
        binding.recViewCategory.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false));
        binding.recViewCategory.setAdapter(homeCategoriesAdapter);


        flashProductAdapter = new HomeProductAdapter(flashProductModelList,activity,this,"flash",country);
        binding.recViewFlashProducts.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false));
        binding.recViewFlashProducts.setAdapter(flashProductAdapter);


        newProductAdapter = new HomeProductAdapter(newProductModelList,activity,this,"new",country);
        binding.recViewNewProducts.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false));
        binding.recViewNewProducts.setAdapter(newProductAdapter);


        recommendedProductAdapter = new HomeProductAdapter(recommendedProductModelList,activity,this,"recommended",country);
        binding.recViewRecommended.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false));
        binding.recViewRecommended.setAdapter(recommendedProductAdapter);



        getSliderData();
        getMainSliderImageData();
        getCategory();
        getProductsData();
    }

    public void getProductsData() {
        country = Paper.book().read("country", "not_selected");
        if (userModel==null){
            if (country.equals("not_selected")){
                country = "em";
            }
        }else {
            if (country.equals("not_selected")){
                if (userModel.getData().getPhone_code().equals("+971")){
                    country = "em";
                }else {
                    country = "eg";
                }
            }
        }

        getNewProducts();
        getFlashProducts();
        getRecommendedProducts();
    }

    private void getSliderData() {
        Api.getService(Tags.base_url)
                .getSlider(lang, country, "slider")
                .enqueue(new Callback<SliderDataModel>() {
                    @Override
                    public void onResponse(Call<SliderDataModel> call, Response<SliderDataModel> response) {
                        binding.progBarSlider.setVisibility(View.GONE);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getData() != null) {
                                if (response.body().getData().size() > 0) {
                                    updateSliderUi(response.body().getData());

                                } else {
                                    binding.flSlider.setVisibility(View.GONE);

                                }
                            }


                        } else {
                            binding.progBarSlider.setVisibility(View.GONE);

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
                    public void onFailure(Call<SliderDataModel> call, Throwable t) {
                        try {
                            binding.progBarSlider.setVisibility(View.GONE);
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

    private void getMainSliderImageData() {
        Api.getService(Tags.base_url)
                .getMainSliderImage(lang, country, "basic")
                .enqueue(new Callback<MainSliderImageDataModel>() {
                    @Override
                    public void onResponse(Call<MainSliderImageDataModel> call, Response<MainSliderImageDataModel> response) {
                        binding.progBarMainImageSlider.setVisibility(View.GONE);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getData() != null) {
                                binding.flMainSliderImage.setVisibility(View.VISIBLE);
                                Picasso.get().load(Uri.parse(Tags.IMAGE_URL+response.body().getData().getMain_image())).into(binding.mainSliderImage);
                            }else {
                                binding.flMainSliderImage.setVisibility(View.GONE);
                            }


                        } else {
                            binding.progBarMainImageSlider.setVisibility(View.GONE);

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
                    public void onFailure(Call<MainSliderImageDataModel> call, Throwable t) {
                        try {
                            binding.progBarMainImageSlider.setVisibility(View.GONE);
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

    private void getCategory()
    {
        Api.getService(Tags.base_url)
                .getCategory(lang, country)
                .enqueue(new Callback<CategoryDataModel>() {
                    @Override
                    public void onResponse(Call<CategoryDataModel> call, Response<CategoryDataModel> response) {
                        binding.progBarCategory.setVisibility(View.GONE);
                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getData() != null) {
                                if (response.body().getData().size()>0){
                                    categoryModelList.clear();
                                    categoryModelList.addAll(response.body().getData());
                                    homeCategoriesAdapter.notifyDataSetChanged();
                                    binding.tvNoDataCategory.setVisibility(View.GONE);

                                }else {
                                    binding.tvNoDataCategory.setVisibility(View.VISIBLE);

                                }
                            }else {
                                binding.tvNoDataCategory.setVisibility(View.VISIBLE);
                            }


                        } else {
                            binding.progBarCategory.setVisibility(View.GONE);

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
                            binding.progBarCategory.setVisibility(View.GONE);
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
    private void getNewProducts()
    {

        newProductModelList.clear();
        newProductAdapter.updateCountry(country);
        newProductAdapter.notifyDataSetChanged();
        binding.tvNoDataNewProduct.setVisibility(View.GONE);
        binding.progBarNewProducts.setVisibility(View.VISIBLE);
        String user_id = null;
        if (userModel!=null){
             user_id = String.valueOf(userModel.getData().getId());
        }
        Api.getService(Tags.base_url)
                .getHomeProducts(lang, country,"new",user_id)
                .enqueue(new Callback<List<ProductModel>>() {
                    @Override
                    public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                        binding.progBarNewProducts.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().size()>0){
                                    newProductModelList.clear();
                                    newProductModelList.addAll(response.body());
                                    newProductAdapter.notifyDataSetChanged();
                                    binding.tvNoDataNewProduct.setVisibility(View.GONE);

                                }else {
                                    binding.tvNoDataNewProduct.setVisibility(View.VISIBLE);

                                }
                            }else {
                                binding.tvNoDataNewProduct.setVisibility(View.VISIBLE);
                            }


                        } else {
                            binding.progBarNewProducts.setVisibility(View.GONE);

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
                    public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                        try {
                            binding.progBarNewProducts.setVisibility(View.GONE);
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
    private void getFlashProducts()
    {
        flashProductModelList.clear();
        flashProductAdapter.updateCountry(country);
        flashProductAdapter.notifyDataSetChanged();
        binding.tvNoDataFlashProduct.setVisibility(View.GONE);
        binding.progFlashProduct.setVisibility(View.VISIBLE);

        String user_id = null;
        if (userModel!=null){
            user_id = String.valueOf(userModel.getData().getId());
        }
        Api.getService(Tags.base_url)
                .getHomeProducts(lang, country,"is_flash",user_id)
                .enqueue(new Callback<List<ProductModel>>() {
                    @Override
                    public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                        binding.progFlashProduct.setVisibility(View.GONE);
                        if (response.isSuccessful()) {

                            if (response.body() != null) {
                                if (response.body().size()>0){
                                    flashProductModelList.clear();
                                    flashProductModelList.addAll(response.body());
                                    flashProductAdapter.notifyDataSetChanged();
                                    binding.tvNoDataFlashProduct.setVisibility(View.GONE);

                                }else {
                                    binding.tvNoDataFlashProduct.setVisibility(View.VISIBLE);

                                }
                            }else {
                                binding.tvNoDataFlashProduct.setVisibility(View.VISIBLE);
                            }


                        } else {
                            binding.progFlashProduct.setVisibility(View.GONE);

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
                    public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                        try {
                            binding.progFlashProduct.setVisibility(View.GONE);
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
    private void getRecommendedProducts()
    {
        recommendedProductModelList.clear();
        recommendedProductAdapter.updateCountry(country);
        recommendedProductAdapter.notifyDataSetChanged();
        binding.tvNoDataRecommended.setVisibility(View.GONE);
        binding.progBarRecommended.setVisibility(View.VISIBLE);

        String user_id = null;
        if (userModel!=null){
             user_id = String.valueOf(userModel.getData().getId());
        }
        Api.getService(Tags.base_url)
                .getHomeProducts(lang, country,"is_recommended",user_id)
                .enqueue(new Callback<List<ProductModel>>() {
                    @Override
                    public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                        binding.progBarRecommended.setVisibility(View.GONE);
                        if (response.isSuccessful()) {

                            if (response.body() != null) {
                                if (response.body().size()>0){
                                    recommendedProductModelList.clear();
                                    recommendedProductModelList.addAll(response.body());
                                    recommendedProductAdapter.notifyDataSetChanged();
                                    binding.tvNoDataRecommended.setVisibility(View.GONE);

                                }else {
                                    binding.tvNoDataRecommended.setVisibility(View.VISIBLE);

                                }
                            }else {
                                binding.tvNoDataRecommended.setVisibility(View.VISIBLE);
                            }


                        } else {
                            binding.progBarRecommended.setVisibility(View.GONE);

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
                    public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                        try {
                            binding.progBarRecommended.setVisibility(View.GONE);
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
    private void updateSliderUi(List<SliderModel> data)
    {
        sliderModelList.clear();
        sliderModelList.addAll(data);
        binding.flSlider.setVisibility(View.VISIBLE);
        sliderAdapter.notifyDataSetChanged();
        if (data.size() > 1) {
            Timer timer = new Timer();
            TimerTask task = new MyTimerTask();
            timer.scheduleAtFixedRate(task, 6000, 6000);
        }
    }

    public void setCategorySelected(int selectedPos) {
        activity.displayFragmentCategory(selectedPos);
    }


    public void add_remove_favorite(ProductModel productModel, int position, String type)
    {

        if (userModel == null) {
            if (productModel.getProduct_likes()!=null){
                productModel.setProduct_likes(null);
            }else {
                productModel.setProduct_likes(new ProductModel.ProductLikes());
            }

            Toast.makeText(activity, R.string.pls_signin_signup, Toast.LENGTH_SHORT).show();
            return;
        }

        newProductModelList.clear();
        newProductAdapter.notifyDataSetChanged();
        binding.tvNoDataNewProduct.setVisibility(View.GONE);
        binding.progBarNewProducts.setVisibility(View.VISIBLE);


        flashProductModelList.clear();
        flashProductAdapter.notifyDataSetChanged();
        binding.tvNoDataFlashProduct.setVisibility(View.GONE);
        binding.progFlashProduct.setVisibility(View.VISIBLE);


        recommendedProductModelList.clear();
        recommendedProductAdapter.notifyDataSetChanged();
        binding.tvNoDataRecommended.setVisibility(View.GONE);
        binding.progBarRecommended.setVisibility(View.VISIBLE);


        String user_id = String.valueOf(userModel.getData().getId());
        Api.getService(Tags.base_url)
                .add_remove_favorite("Bearer "+userModel.getData().getToken(),user_id, String.valueOf(productModel.getId()))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                getProductsData();
                            }


                        } else {

                            try {
                                Log.e("errorNotCode", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }




                            if (productModel.getProduct_likes()!=null){
                                productModel.setProduct_likes(null);
                            }else {
                                productModel.setProduct_likes(new ProductModel.ProductLikes());
                            }

                            if (type.equals("new")){
                                newProductModelList.set(position,productModel);
                                newProductAdapter.notifyItemChanged(position);
                            }else if (type.equals("flash")){
                                flashProductModelList.set(position,productModel);
                                flashProductAdapter.notifyItemChanged(position);
                            }else {
                                recommendedProductModelList.set(position,productModel);
                                recommendedProductAdapter.notifyItemChanged(position);
                            }

                            if (response.code() == 500) {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            if (productModel.getProduct_likes()!=null){
                                productModel.setProduct_likes(null);
                            }else {
                                productModel.setProduct_likes(new ProductModel.ProductLikes());
                            }

                            if (type.equals("new")){
                                newProductModelList.set(position,productModel);
                                newProductAdapter.notifyItemChanged(position);
                            }else if (type.equals("flash")){
                                flashProductModelList.set(position,productModel);
                                flashProductAdapter.notifyItemChanged(position);
                            }else {
                                recommendedProductModelList.set(position,productModel);
                                recommendedProductAdapter.notifyItemChanged(position);
                            }
                            if (t.getMessage() != null) {
                                Log.e("error_not_code", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    public void setItemData(ProductModel model) {
        Intent intent = new Intent(activity, ProductDetailsActivity.class);
        intent.putExtra("data",model);
        startActivityForResult(intent,100);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode== Activity.RESULT_OK){
            getProductsData();
        }
    }

    public class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            activity.runOnUiThread(() -> {
                if (binding.pager.getCurrentItem() < sliderModelList.size() - 1) {
                    binding.pager.setCurrentItem(binding.pager.getCurrentItem() + 1);

                } else {
                    binding.pager.setCurrentItem(0);
                }
            });
        }
    }
}
