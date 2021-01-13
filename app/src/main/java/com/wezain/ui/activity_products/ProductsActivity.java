package com.wezain.ui.activity_products;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wezain.R;
import com.wezain.adapters.ProductAdapter;
import com.wezain.databinding.ActivityProductsBinding;
import com.wezain.language.Language;
import com.wezain.models.LoginModel;
import com.wezain.models.ProductModel;
import com.wezain.models.SubDepartmentModel;
import com.wezain.models.UserModel;
import com.wezain.preferences.Preferences;
import com.wezain.remote.Api;
import com.wezain.tags.Tags;
import com.wezain.ui.activity_product_details.ProductDetailsActivity;
import com.wezain.ui.activity_sign_up.SignUpActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsActivity extends AppCompatActivity {
    private ActivityProductsBinding binding;
    private UserModel userModel;
    private Preferences preferences;
    private SubDepartmentModel subDepartmentModel;
    private String lang;
    private String country;
    private ProductAdapter adapter;
    private List<ProductModel> productModelList;
    private boolean isFavoriteChanged = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang","ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_products);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        subDepartmentModel = (SubDepartmentModel) intent.getSerializableExtra("data");
    }


    private void initView() {
        productModelList = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
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
        binding.setLang(lang);
        binding.setModel(subDepartmentModel);
        adapter = new ProductAdapter(productModelList,this,country);
        binding.recView.setLayoutManager(new GridLayoutManager(this,2));
        binding.recView.setAdapter(adapter);
        binding.llBack.setOnClickListener(view -> onBackPressed());

        getProducts();


    }

    private void getProducts()
    {
        productModelList.clear();
        adapter.notifyDataSetChanged();
        binding.tvNoData.setVisibility(View.GONE);
        binding.progBar.setVisibility(View.VISIBLE);
        String user_id = null;
        if (userModel!=null){
            user_id = String.valueOf(userModel.getData().getId());
        }
        Api.getService(Tags.base_url)
                .getProducts(lang, country,"department_id",subDepartmentModel.getId(),user_id)
                .enqueue(new Callback<List<ProductModel>>() {
                    @Override
                    public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {

                            if (response.body() != null) {
                                if (response.body().size()>0){
                                    productModelList.clear();
                                    productModelList.addAll(response.body());
                                    adapter.notifyDataSetChanged();
                                    binding.tvNoData.setVisibility(View.GONE);

                                }else {
                                    binding.tvNoData.setVisibility(View.VISIBLE);

                                }
                            }else {
                                binding.tvNoData.setVisibility(View.VISIBLE);
                            }


                        } else {
                            binding.progBar.setVisibility(View.GONE);

                            switch (response.code()) {
                                case 500:
                                    Toast.makeText(ProductsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    break;

                                default:
                                    Toast.makeText(ProductsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                            binding.progBar.setVisibility(View.GONE);
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(ProductsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else if (t.getMessage().toLowerCase().contains("socket") || t.getMessage().toLowerCase().contains("canceled")) {
                                } else {
                                    Toast.makeText(ProductsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode==RESULT_OK){
            isFavoriteChanged = true;
        }
    }

    @Override
    public void onBackPressed() {
        if (isFavoriteChanged){
            setResult(RESULT_OK);
        }
        finish();
    }

    public void add_remove_favorite(ProductModel productModel, int position)
    {
        if (userModel == null) {
            if (productModel.getProduct_likes()!=null){
                productModel.setProduct_likes(null);
            }else {
                productModel.setProduct_likes(new ProductModel.ProductLikes());
            }

            Toast.makeText(this, R.string.pls_signin_signup, Toast.LENGTH_SHORT).show();
            return;
        }
        String user_id = String.valueOf(userModel.getData().getId());
        Api.getService(Tags.base_url)
                .add_remove_favorite("Bearer "+userModel.getData().getToken(),user_id, String.valueOf(productModel.getId()))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                isFavoriteChanged = true;
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



                            if (response.code() == 500) {
                                Toast.makeText(ProductsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProductsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


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


                            if (t.getMessage() != null) {
                                Log.e("error_not_code", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(ProductsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ProductsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    public void setItemData(ProductModel model) {
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra("data",model);
        startActivityForResult(intent,100);
    }


}