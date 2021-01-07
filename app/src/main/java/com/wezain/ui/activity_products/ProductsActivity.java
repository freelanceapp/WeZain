package com.wezain.ui.activity_products;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wezain.R;
import com.wezain.adapters.ProductAdapter2;
import com.wezain.databinding.ActivityProductsBinding;
import com.wezain.language.Language;
import com.wezain.models.DepartmentModel;
import com.wezain.models.ProductModel;

import com.wezain.mvp.activity_product_mvp.ActivityProductPresenter;
import com.wezain.mvp.activity_product_mvp.ActivityProductView;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class ProductsActivity extends AppCompatActivity implements ActivityProductView {
    private ActivityProductsBinding binding;
    private String lang;
    private DepartmentModel departmentModel;

    private List<ProductModel> productModelList;
    private ProductAdapter2 adapter;
    private ActivityProductPresenter presenter;
    private String country;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
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
        departmentModel = (DepartmentModel) intent.getSerializableExtra("data1");


    }

    private void initView() {
        productModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        country = Paper.book().read("country", "eg");

        binding.setLang(lang);
        binding.setModel(departmentModel);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter2(productModelList,this);
        binding.recView.setAdapter(adapter);

        presenter= new ActivityProductPresenter(this,this);
        presenter.getProducts(String.valueOf(departmentModel.getId()),lang,country);

        binding.imageBack.setOnClickListener(view -> finish());

    }


    @Override
    public void onSuccess(List<ProductModel> data) {
        productModelList.clear();
        if (data.size()>0){
            productModelList.addAll(data);
            adapter.notifyDataSetChanged();
            binding.tvNoData.setVisibility(View.GONE);
        }else {
            binding.tvNoData.setVisibility(View.VISIBLE);

        }

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressShow() {
        binding.progBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgressHide() {
        binding.progBar.setVisibility(View.GONE);

    }
}