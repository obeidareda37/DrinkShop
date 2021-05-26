package com.example.drinkshop.database.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.drinkshop.database.modeldb.Cart;
import com.example.drinkshop.database.modeldb.Favorite;

@Database(entities = {Cart.class, Favorite.class}, version = 4, exportSchema = false)
public abstract class DrinkShopDatabase extends RoomDatabase {

    public abstract CartDao cartDao();
    public abstract FavoriteDao favoriteDao();

    private static DrinkShopDatabase instance;

    public static DrinkShopDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, DrinkShopDatabase.class, "DrinkShopDB")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
