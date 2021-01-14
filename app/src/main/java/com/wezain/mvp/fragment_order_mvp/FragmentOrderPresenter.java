package com.wezain.mvp.fragment_order_mvp;

import android.content.Context;
import android.util.Log;


import com.wezain.R;
import com.wezain.models.OrderDataModel;
import com.wezain.models.UserModel;
import com.wezain.preferences.Preferences;
import com.wezain.remote.Api;
import com.wezain.tags.Tags;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentOrderPresenter {
    private Context context;
    private FragmentOrderView view;
    private Preferences preference;
    private UserModel userModel;

    public FragmentOrderPresenter(Context context, FragmentOrderView view) {
        this.context = context;
        this.view = view;
        preference = Preferences.getInstance();
        userModel = preference.getUserData(context);

    }



    public void getOrders(String lang,String country)
    {
        if (userModel == null) {
            return;
        }
        String user_id = String.valueOf(userModel.getData().getId());
        view.onProgressShow();
        Api.getService(Tags.base_url)
                .getOrders(user_id,lang,country,"new")
                .enqueue(new Callback<OrderDataModel>() {
                    @Override
                    public void onResponse(Call<OrderDataModel> call, Response<OrderDataModel> response) {
                        view.onProgressHide();
                        if (response.isSuccessful()) {
                            if (response.body() != null ) {
                                view.onSuccess(response.body().getData());

                            }


                        } else {
                            view.onProgressHide();
                            try {
                                Log.e("errorNotCode", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                view.onFailed("Server Error");

                            } else {
                                view.onFailed(context.getString(R.string.failed));

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderDataModel> call, Throwable t) {
                        try {
                            view.onProgressHide();


                            if (t.getMessage() != null) {
                                Log.e("error_not_code", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    view.onFailed(context.getString(R.string.something));
                                } else {
                                    view.onFailed(context.getString(R.string.failed));

                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }



}
