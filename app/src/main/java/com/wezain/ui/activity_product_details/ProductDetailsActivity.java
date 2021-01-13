package com.wezain.ui.activity_product_details;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.wezain.R;
import com.wezain.adapters.PriceAdapter;
import com.wezain.adapters.ProductSliderAdapter;
import com.wezain.databinding.ActivityProductDetailsBinding;
import com.wezain.language.Language;
import com.wezain.models.ProductModel;
import com.wezain.models.SingleProductModel;
import com.wezain.models.UserModel;
import com.wezain.preferences.Preferences;
import com.wezain.remote.Api;
import com.wezain.tags.Tags;
import com.wezain.ui.activity_products.ProductsActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsActivity extends AppCompatActivity {
    private ActivityProductDetailsBinding binding;
    private String lang;;
    private ProductSliderAdapter sliderAdapter;
    private PriceAdapter priceAdapter;
    private ProductModel productModel;
    private int amount = 1;
    private boolean isDataChanged = false;
    private String  country;
    private Preferences preferences;
    private UserModel userModel;



    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_details);
        getDataFromIntent();
        initView();


    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        productModel = (ProductModel) intent.getSerializableExtra("data");

    }

    private void initView() {
        preferences = Preferences.getInstance();
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.setModel(productModel);
        userModel = preferences.getUserData(this);
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


        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);




        binding.imageIncrease.setOnClickListener(view -> {
            amount++;
            binding.tvAmount.setText(String.valueOf(amount));
        });

        binding.imageDecrease.setOnClickListener(view -> {
            if (amount > 1) {
                amount--;
                binding.tvAmount.setText(String.valueOf(amount));
            }
        });


       // binding.flAddToCart.setOnClickListener(view -> createDialogAlert());

        binding.llBack.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.checkbox.setOnClickListener(view -> {
            if (binding.checkbox.isChecked()) {
                productModel.setProduct_likes(new ProductModel.ProductLikes());

            } else {
                productModel.setProduct_likes(null);

            }

            add_remove_favorite();
        });

        binding.flCart.setOnClickListener(view -> {
            /*Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);*/
        });


        getProductById(productModel.getId());


    }

    public void getProductById(int product_id)
    {
        String user_id = null;
        if (userModel != null) {
            user_id = String.valueOf(userModel.getData().getId());
        }
        Api.getService(Tags.base_url)
                .getProductDetails(lang,country,product_id,user_id)
                .enqueue(new Callback<SingleProductModel>() {
                    @Override
                    public void onResponse(Call<SingleProductModel> call, Response<SingleProductModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getData() != null) {
                                updateUi(response.body().getData());
                            }


                        } else {
                            try {
                                Log.e("errorNotCode", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(ProductDetailsActivity.this,"Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(ProductDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SingleProductModel> call, Throwable t) {
                        try {


                            if (t.getMessage() != null) {
                                Log.e("error_not_code", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(ProductDetailsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ProductDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void updateUi(ProductModel data) {
        this.productModel = data;
        binding.setModel(data);
        binding.view.setVisibility(View.GONE);

        if (!productModel.getPrice_type().equals("single")&&productModel.getProduct_prices()!=null&&productModel.getProduct_prices().size()>0){
            binding.recView.setLayoutManager(new LinearLayoutManager(this));
            priceAdapter = new PriceAdapter(productModel.getProduct_prices(),this,country);
            binding.recView.setAdapter(priceAdapter);
        }

        if (productModel.getProduct_images().size() > 0) {
            List<ProductModel.Product_Images> galleryModelList = new ArrayList<>(productModel.getProduct_images());
            sliderAdapter = new ProductSliderAdapter(galleryModelList, this);
            binding.pager.setAdapter(sliderAdapter);
            binding.flSlider.setVisibility(View.VISIBLE);
            binding.image.setVisibility(View.GONE);

        } else {
            binding.image.setVisibility(View.VISIBLE);
            binding.flSlider.setVisibility(View.GONE);

        }
    }


    public void add_remove_favorite()
    {
        if (userModel == null) {
            if (productModel.getProduct_likes()!=null){
                productModel.setProduct_likes(null);
            }else {
                productModel.setProduct_likes(new ProductModel.ProductLikes());
            }
            binding.setModel(productModel);
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
                                isDataChanged = true;
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
                            binding.setModel(productModel);



                            if (response.code() == 500) {
                                Toast.makeText(ProductDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProductDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


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
                            binding.setModel(productModel);


                            if (t.getMessage() != null) {
                                Log.e("error_not_code", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(ProductDetailsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ProductDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }




    @Override
    protected void onResume() {
        super.onResume();



    }

    @Override
    public void onBackPressed() {
        if (isDataChanged) {
            setResult(RESULT_OK);
        }
        finish();
    }
}