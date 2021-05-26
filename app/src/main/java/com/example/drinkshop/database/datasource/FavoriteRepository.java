package com.example.drinkshop.database.datasource;

import com.example.drinkshop.database.local.FavoriteDao;
import com.example.drinkshop.database.local.FavoriteDataSource;
import com.example.drinkshop.database.modeldb.Favorite;

import java.util.List;

import io.reactivex.Flowable;


public class FavoriteRepository  implements IFavoriteDataSource{

    private IFavoriteDataSource favoriteDataSource;

    public FavoriteRepository(IFavoriteDataSource favoriteDataSource) {
        this.favoriteDataSource = favoriteDataSource;
    }

    private static  FavoriteRepository instance;


    public static FavoriteRepository getInstance(IFavoriteDataSource favoriteDataSource) {
        if (instance == null) {
            instance = new FavoriteRepository(favoriteDataSource);
        }
        return instance;
    }
    @Override
    public Flowable<List<Favorite>> getFavoriteItems() {
        return favoriteDataSource.getFavoriteItems();
    }

    @Override
    public int isFavorite(int itemId) {
        return favoriteDataSource.isFavorite(itemId);
    }

    @Override
    public void insertFav(Favorite... favorites) {
        favoriteDataSource.insertFav(favorites);

    }

    @Override
    public void delete(Favorite favorite) {
        favoriteDataSource.delete(favorite);
    }
}
