package com.wezain.services;


import com.wezain.models.CategoryDataModel;
import com.wezain.models.MainSliderImageDataModel;
import com.wezain.models.ProductDataModel;
import com.wezain.models.ProductModel;
import com.wezain.models.SliderDataModel;
import com.wezain.models.UserModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> login(@Field("email") String email,
                          @Field("password") String password
    );

    @FormUrlEncoded
    @POST("api/register")
    Call<UserModel> signUpWithoutImage(@Field("first_name") String first_name,
                                       @Field("last_name") String last_name,
                                       @Field("phone_code") String phone_code,
                                       @Field("phone") String phone,
                                       @Field("email") String email,
                                       @Field("password") String password
    );


    @Multipart
    @POST("api/register")
    Call<UserModel> signUpWithImage(@Part("first_name") RequestBody first_name,
                                    @Part("last_name") RequestBody last_name,
                                    @Part("phone_code") RequestBody phone_code,
                                    @Part("phone") RequestBody phone,
                                    @Part("email") RequestBody email,
                                    @Part("password") RequestBody password,
                                    @Part MultipartBody.Part logo
    );

}