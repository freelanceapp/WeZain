package com.wezain.mvp.activity_my_address_mvp;

import com.wezain.models.AddressModel;

import java.util.List;

public interface ActivityMyAddressView {
    void onSuccess(List<AddressModel> data);
    void onFailed(String msg);
    void onProgressShow();
    void onProgressHide();
    void onRemovedSuccess();


}
