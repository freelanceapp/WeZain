package com.wezain.mvp.activity_cart_mvp;


import com.wezain.models.CartDataModel;
import com.wezain.models.SingleOrderModel;

public interface CartActivityView {
    void onFinished();
    void onCheckOut();
    void onDataSuccess(CartDataModel cartDataModel);
    void onCartItemRemoved(int pos);
    void onCostUpdate(double totalItemCost);
    void onFailed(String msg);
    void onOrderSendSuccessfully(SingleOrderModel singleOrderModel);




}
