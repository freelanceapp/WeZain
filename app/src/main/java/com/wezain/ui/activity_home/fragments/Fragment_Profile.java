package com.wezain.ui.activity_home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.wezain.R;
import com.wezain.databinding.FragmentProfileBinding;
import com.wezain.interfaces.Listeners;
import com.wezain.models.UserModel;
import com.wezain.preferences.Preferences;
import com.wezain.share.Common;
import com.wezain.ui.activity_favorite.FavoriteActivity;
import com.wezain.ui.activity_home.HomeActivity;
import com.wezain.ui.activity_select_address.SelectAddressActivity;

import io.paperdb.Paper;

public class Fragment_Profile extends Fragment implements Listeners.ProfileActions{
    private FragmentProfileBinding binding;
    private String lang;
    private HomeActivity activity;
    private Preferences preferences;
    private UserModel userModel;

    public static Fragment_Profile newInstance(){
        return new Fragment_Profile();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        if (userModel!=null){

        }
        binding.setActions(this);
    }

    @Override
    public void onMyWallet() {

    }

    @Override
    public void onFavorite() {
        userModel = preferences.getUserData(activity);
        if (userModel!=null){
            Intent intent = new Intent(activity, FavoriteActivity.class);
            startActivityForResult(intent,100);
        }else {
            Common.CreateDialogAlert(activity,getString(R.string.pls_signin_signup));
        }

    }

    @Override
    public void onAddress() {
        userModel = preferences.getUserData(activity);
        if (userModel!=null){
            Intent intent = new Intent(activity, SelectAddressActivity.class);
            startActivity(intent);
        }else {
            Common.CreateDialogAlert(activity,getString(R.string.pls_signin_signup));
        }
    }

    @Override
    public void onChangeLanguage() {

    }

    @Override
    public void onTerms() {

    }

    @Override
    public void onContactUs() {

    }

    @Override
    public void onMenu() {

    }

    @Override
    public void onFacebook() {

    }

    @Override
    public void onTwitter() {

    }

    @Override
    public void onInstagram() {

    }

    @Override
    public void onLogout() {

    }

}
