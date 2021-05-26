package com.example.drinkshop.network;

import com.example.drinkshop.model.Banner;
import com.example.drinkshop.model.Category;
import com.example.drinkshop.model.CheckUserResponse;
import com.example.drinkshop.model.Drink;
import com.example.drinkshop.model.Order;
import com.example.drinkshop.model.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IDrinkShopAPI {

    @FormUrlEncoded
    @POST("checkuser.php")
    Call<CheckUserResponse> checkUserExists(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("register.php")
    Call<User> registerNewUser(@Field("phone") String phone,
                               @Field("name") String name,
                               @Field("address") String address,
                               @Field("birthdate") String birthdate);

    @FormUrlEncoded
    @POST("getdrink.php")
    Observable<List<Drink>> getDrink(@Field("menuid") String menuID);

    @GET("getbanner.php")
    Observable<List<Banner>> getBanners();

    @GET("getalldrinks.php")
    Observable<List<Drink>> getAllDrinks();

    @GET("getmenu.php")
    Observable<List<Category>> getMenu();

    @FormUrlEncoded
    @POST("submitorder.php")
    Call<String> submitOrder(@Field("price") float orderPrice,
                            @Field("orderDetail") String orderDetail,
                            @Field("address") String orderAddress,
                            @Field("phone") String orderPhone,
                            @Field("comment") String comment);

}
