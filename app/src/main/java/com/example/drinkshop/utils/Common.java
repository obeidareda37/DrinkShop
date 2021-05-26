package com.example.drinkshop.utils;

import com.example.drinkshop.database.datasource.CartRepository;
import com.example.drinkshop.database.datasource.FavoriteRepository;
import com.example.drinkshop.database.local.DrinkShopDatabase;
import com.example.drinkshop.model.Category;
import com.example.drinkshop.model.Drink;
import com.example.drinkshop.network.IDrinkShopAPI;
import com.example.drinkshop.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

public class Common {
    //In Emulator, localhost = 10.0.2.2
    private static final String BASE_URL = "http://10.0.2.2/drinkshop/";

    public static final String TOPPING_MENU_ID ="7";
    public static Category currentCategory=null;
    public static List<Drink> toppingList = new ArrayList<>();

    public static double toppingPrice=0.0;
    public static List<String> toppingAdded = new ArrayList<>();

    //Hold field
    public static int sizeOfCup=-1;//-1 : mo choose (error), 0:M,I,L
    public static int sugar =-1; //-1 : no choose (error)
    public static int ice = -1;


    //Database
    public static DrinkShopDatabase drinkShopDatabase;
    public static CartRepository cartRepository;
    public static FavoriteRepository favoriteRepository;


    public static IDrinkShopAPI getAPI(){
        return RetrofitClient.getClient(BASE_URL).create(IDrinkShopAPI.class);
    }
}
