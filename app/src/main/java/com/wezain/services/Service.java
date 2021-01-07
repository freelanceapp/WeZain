package com.wezain.services;


import com.wezain.models.AddFavoriteDataModel;
import com.wezain.models.AddressDataModel;
import com.wezain.models.CategoryDataModel;
import com.wezain.models.MainCategoryDataModel;
import com.wezain.models.MainSliderImageDataModel;
import com.wezain.models.PlaceGeocodeData;
import com.wezain.models.PlaceMapDetailsData;
import com.wezain.models.ProductDataModel;
import com.wezain.models.ProductModel;
import com.wezain.models.SliderDataModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Service {
    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );

    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);

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
    @GET("api/Get-departments")
    Call<MainCategoryDataModel> getCategorywithsub(@Query("lang") String lang,
                                                   @Query("country") String country);
    @GET("api/Get-products-by-department-id")
    Call<ProductDataModel> getProducts(@Query("lang") String lang,
                                       @Query("country") String country,
                                       @Query("department_type") String department_type,
                                       @Query("department_id") String department_id,
                                       @Query("user_id") String user_id,
                                       @Query("pagination_status")String pagination_status



    );
    @FormUrlEncoded
    @POST("api/action-wishlists")
    Call<AddFavoriteDataModel> add_remove_favorite(@Header("Authorization") String token,
                                                   @Field("user_id") String user_id,
                                                   @Field("product_id") String product_id
    );

    @GET("api/my-wishlists")
    Call<ProductDataModel> getMyFavorite(@Header("Authorization") String token,
                                         @Query("user_id") String user_id
    );

    @GET("api/my-address")
    Call<AddressDataModel> getMyAddress(@Header("Authorization") String token,
                                        @Query("user_id") String user_id
    );


    @FormUrlEncoded
    @POST("api/add-address")
    Call<ResponseBody> addAddress(@Header("Authorization") String token,
                                  @Field("user_id") String user_id,
                                  @Field("phone") String phone,
                                  @Field("name") String name,
                                  @Field("address") String address,
                                  @Field("google_lat") double google_lat,
                                  @Field("google_long") double google_long,
                                  @Field("type") String type
    );

    @FormUrlEncoded
    @POST("api/edit-address")
    Call<ResponseBody> updateAddress(@Header("Authorization") String token,
                                     @Field("id") String address_id,
                                     @Field("user_id") String user_id,
                                     @Field("phone") String phone,
                                     @Field("name") String name,
                                     @Field("address") String address,
                                     @Field("google_lat") double google_lat,
                                     @Field("google_long") double google_long,
                                     @Field("type") String type
    );

    @FormUrlEncoded
    @POST("api/delete-address")
    Call<ResponseBody> deleteAddress(@Header("Authorization") String token,
                                     @Field("id") String address_id
    );


}