package com.wezain.ui.activity_cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.wezain.R;
import com.wezain.adapters.CartAdapter;
import com.wezain.databinding.ActivityCartBinding;
import com.wezain.language.Language;
import com.wezain.models.CartDataModel;
import com.wezain.models.SendOrderModel;
import com.wezain.models.SingleOrderModel;
import com.wezain.models.UserModel;
import com.wezain.mvp.activity_cart_mvp.ActivityCartPresenter;
import com.wezain.mvp.activity_cart_mvp.CartActivityView;
import com.wezain.preferences.Preferences;
import com.wezain.ui.activity_check_out.CheckOutActivity;
import com.wezain.ui.activity_order_details.OrderDetailsActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class CartActivity extends AppCompatActivity implements CartActivityView {
    private ActivityCartBinding binding;
    private ActivityCartPresenter presenter;
    private String lang;
    private CartAdapter adapter;
    private List<CartDataModel.CartModel> cartModelList;
    private String country;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        initView();
    }


    private void initView()
    {
        cartModelList = new ArrayList<>();
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
        presenter = new ActivityCartPresenter(this, this);

        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(cartModelList, this,country);
        binding.recView.setAdapter(adapter);
        presenter.getCartData();
        binding.llBack.setOnClickListener(view -> {
            presenter.backPress();
        });

        binding.btnNext.setOnClickListener(view -> presenter.checkOut());
    }

    @Override
    public void onBackPressed() {
        presenter.backPress();
    }


    @Override
    public void onFinished() {
        finish();
    }

    @Override
    public void onCheckOut() {

        Intent intent = new Intent(this, CheckOutActivity.class);
        startActivityForResult(intent,100);
    }

    @Override
    public void onDataSuccess(CartDataModel cartDataModel) {
        cartModelList.clear();
        cartModelList.addAll(cartDataModel.getProducts());
        if (cartModelList.size() > 0) {
            adapter.notifyDataSetChanged();
            binding.llCost.setVisibility(View.VISIBLE);
            binding.llEmptyCart.setVisibility(View.GONE);
        } else {
            binding.llCost.setVisibility(View.GONE);
            binding.llEmptyCart.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCartItemRemoved(int pos) {
        cartModelList.remove(pos);
        adapter.notifyItemRemoved(pos);
        if (cartModelList.size() > 0) {
            adapter.notifyDataSetChanged();
            binding.llCost.setVisibility(View.VISIBLE);
            binding.llEmptyCart.setVisibility(View.GONE);
        } else {
            binding.llCost.setVisibility(View.GONE);
            binding.llEmptyCart.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onCostUpdate(double totalItemCost) {
        binding.tvTotalItemCost.setText(String.format(Locale.ENGLISH, "%.2f %s", totalItemCost, country.equals("em") ? getString(R.string.aed) : getString(R.string.tl)));

    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOrderSendSuccessfully(SingleOrderModel singleOrderModel) {
        Toast.makeText(this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, OrderDetailsActivity.class);
        intent.putExtra("data",singleOrderModel.getData());
        startActivity(intent);
        finish();
    }


    public void removeCartItem(CartDataModel.CartModel model) {
        presenter.removeCartItem(model);
    }

    public void increase_decrease_item_count(CartDataModel.CartModel cartModel, int amount) {
        presenter.update_cart(cartModel, amount);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            SendOrderModel model = (SendOrderModel) data.getSerializableExtra("data");
            presenter.sendOrder(model,lang);
        }
    }
}