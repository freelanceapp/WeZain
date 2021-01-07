package com.wezain.services;


import com.wezain.models.CategoryDataModel;
import com.wezain.models.MainSliderImageDataModel;
import com.wezain.models.ProductDataModel;
import com.wezain.models.ProductModel;
import com.wezain.models.SliderDataModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {
    @GET("api/get-offers")
    Call<SliderDataModel> getSlider(@Query("lang") String lang,
                                    @Query("country") String country,
                                    @Query("offer_type") String offer_type
    );

    @GET("api/get-offers")
    Call<MainSliderImageDataModel> getMainSliderImage(@Query("lang") String lang,
                                                      @Query("country") String country,
                                                      @Query("offer_type") String offer_type
    );

    @GET("api/Get-departments")
    Call<CategoryDataModel> getCategory(@Query("lang") String lang,
                                        @Query("country") String country);

    @GET("api/Get-products-type")
    Call<List<ProductModel>> getHomeProducts(@Query("lang") String lang,
                                             @Query("country") String country,
                                             @Query("product_type") String product_type,
                                             @Query("user_id") String user_id
                                           );

}