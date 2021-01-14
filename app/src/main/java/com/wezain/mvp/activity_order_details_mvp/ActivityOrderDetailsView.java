package com.wezain.mvp.activity_order_details_mvp;


import com.wezain.models.SingleOrderModel;

public interface ActivityOrderDetailsView {

    void onSuccess(SingleOrderModel data);

    void onFailed(String msg);

    void onProgressShow();

    void onProgressHide();


}
