package com.wezain.ui.activity_order_details;

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
import com.wezain.adapters.OrderProductAdapter;
import com.wezain.models.SingleOrderModel;
import com.wezain.databinding.ActivityOrderDetailsBinding;
import com.wezain.language.Language;
import com.wezain.models.OrderModel;
import com.wezain.models.UserModel;
import com.wezain.mvp.activity_order_details_mvp.ActivityOrderDetailsPresenter;
import com.wezain.mvp.activity_order_details_mvp.ActivityOrderDetailsView;
import com.wezain.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class OrderDetailsActivity extends AppCompatActivity implements ActivityOrderDetailsView {
    private ActivityOrderDetailsBinding binding;
    private ActivityOrderDetailsPresenter presenter;
    private List<OrderModel.OrderDetailsProduct> cartProductModelList;
    private OrderProductAdapter adapter;
    private OrderModel orderModel;
    private String lang;
    private String country;
    private Preferences preferences;
    private UserModel userModel;
    public static String currency="";
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_details);
        getDataFromIntent();
        initView();


    }

    private void initView() {
        cartProductModelList = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        country = Paper.book().read("country", "not_selected");

        if (userModel == null) {
            if (country.equals("not_selected")) {
                country = "em";
            }
        } else {
            if (country.equals("not_selected")) {
                if (userModel.getData().getPhone_code().equals("+971")) {
                    country = "em";
                } else {
                    country = "eg";
                }
            }
        }
        if (orderModel.getBill_currency().equals("eg")){
            currency=getString(R.string.tl);
        }else {
            currency=getString(R.string.aed);

        }

        binding.setCountry(country);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        adapter = new OrderProductAdapter(cartProductModelList,this,country,orderModel);
        binding.recView.setAdapter(adapter);
        presenter = new ActivityOrderDetailsPresenter(this,this);
        presenter.getProduct(orderModel,lang,country);
        binding.llBack.setOnClickListener(view -> finish());

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        orderModel = (OrderModel) intent.getSerializableExtra("data");

    }


    @Override
    public void onSuccess(SingleOrderModel data) {
        binding.view.setVisibility(View.GONE);
        orderModel = data.getData();
        binding.setModel(orderModel);

        if (data.getData().getOrder_details_product()!=null&&data.getData().getOrder_details_product().size()>0){
            cartProductModelList.clear();
            cartProductModelList.addAll(data.getData().getOrder_details_product());
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProgressShow() {
        binding.view.setVisibility(View.VISIBLE);
        binding.progBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgressHide() {
        binding.progBar.setVisibility(View.GONE);

    }
}