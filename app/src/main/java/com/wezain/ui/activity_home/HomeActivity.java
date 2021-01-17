package com.wezain.ui.activity_home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wezain.R;
import com.wezain.databinding.ActivityHomeBinding;
import com.wezain.language.Language;
import com.wezain.models.CartDataModel;
import com.wezain.mvp.activity_home_mvp.ActivityHomePresenter;
import com.wezain.mvp.activity_home_mvp.HomeActivityView;
import com.wezain.preferences.Preferences;
import com.wezain.ui.activity_cart.CartActivity;
import com.wezain.ui.activity_login.LoginActivity;
import com.wezain.ui.activity_search.SearchActivity;

import java.util.List;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements HomeActivityView {
    private ActivityHomeBinding binding;
    private FragmentManager fragmentManager;
    private ActivityHomePresenter presenter;
    private boolean onCategorySelected = false;
    private CartDataModel cartDataModel;
    private Preferences preferences;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang","ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();
    }



    private void initView() {
        preferences = Preferences.getInstance();

        fragmentManager = getSupportFragmentManager();
        presenter = new ActivityHomePresenter(this, this, fragmentManager);
        binding.navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (!onCategorySelected){
                    presenter.manageFragments(item);

                }
                onCategorySelected = false;
                return true;
            }
        });
        binding.flCart.setOnClickListener(view -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });

        binding.flSearch.setOnClickListener(view -> {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivityForResult(intent,100);
        });



    }


    public void refreshFragmentHome(){
        presenter.refreshFragmentHome();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode==100&&resultCode==RESULT_OK){
            refreshFragmentHome();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onCategoryFragmentSelected() {
        onCategorySelected = true;
        binding.navigationView.setSelectedItemId(R.id.categories);

    }

    @Override
    public void onBackPressed() {
        presenter.backPress();
    }

    @Override
    public void onHomeFragmentSelected() {
        binding.navigationView.setSelectedItemId(R.id.home);
    }


    @Override
    public void onNavigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

    public void displayFragmentCategory(int selectedPos){
        presenter.displayFragmentCategories(selectedPos);

    }

    public void logout(){
        preferences.clear(this);
        onNavigateToLoginActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cartDataModel = preferences.getCartData(this);
        if (cartDataModel!=null){
            int count = cartDataModel.getProducts().size();
            binding.setCount(count);
        }else {
            binding.setCount(0);
        }
    }

    @Override
    public void onFinished() {
        finish();
    }

    public void refreshActivity(String lang) {
        Paper.init(this);
        Paper.book().write("lang",lang);
        Language.updateResources(this,lang);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void updateCartCount(int count) {
        binding.setCount(count);
    }
}