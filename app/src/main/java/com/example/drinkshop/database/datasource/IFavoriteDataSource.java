package com.example.drinkshop.database.datasource;

import com.example.drinkshop.database.modeldb.Favorite;

import java.util.List;

import io.reactivex.Flowable;

public interface IFavoriteDataSource {

    Flowable<List<Favorite>> getFavoriteItems();
    int isFavorite(int itemId);
    void insertFav(Favorite...favorites);
    void delete(Favorite favorite);

}
