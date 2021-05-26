package com.example.drinkshop.database.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.drinkshop.database.modeldb.Favorite;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM favorite")
    Flowable<List<Favorite>> getFavoriteItems();

    @Query("SELECT EXISTS (SELECT 1 FROM Favorite WHERE id=:itemId)")
    int isFavorite(int itemId);

    @Insert
    void insertFav(Favorite...favorites);

    @Delete
    void delete(Favorite favorite);
}
