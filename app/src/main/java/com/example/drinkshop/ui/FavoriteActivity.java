package com.example.drinkshop.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.drinkshop.R;
import com.example.drinkshop.adapters.FavoriteAdapter;
import com.example.drinkshop.database.modeldb.Cart;
import com.example.drinkshop.database.modeldb.Favorite;
import com.example.drinkshop.utils.Common;
import com.example.drinkshop.utils.RecyclerItemTouchHelper;
import com.example.drinkshop.utils.RecyclerItemTouchHelperListener;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class FavoriteActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {
    RecyclerView recyclerView;
    CompositeDisposable compositeDisposable;
    FavoriteAdapter favoriteAdapter;
    List<Favorite> localFavorites = new ArrayList<>();
    ConstraintLayout rootLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        compositeDisposable = new CompositeDisposable();
        recyclerView = findViewById(R.id.recycler_fav);
        rootLayout = findViewById(R.id.rootLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        loadFavoritesItem();

    }

    private void loadFavoritesItem() {
        compositeDisposable.add(
                Common.favoriteRepository.getFavoriteItems()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<List<Favorite>>() {
                            @Override
                            public void accept(List<Favorite> favorites) throws Exception {
                                displayFavoritesItem(favorites);
                            }
                        })
        );

        RxJavaPlugins.setErrorHandler(e -> {
            if (e instanceof UndeliverableException) {
                e = e.getCause();
            }
            if ((e instanceof IOException) || (e instanceof SocketException)) {
                // fine, irrelevant network problem or API that throws on cancellation
                return;
            }
            if (e instanceof InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return;
            }

            Log.d("Undeliverable", e.getMessage());
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavoritesItem();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private void displayFavoritesItem(List<Favorite> favorites) {
        localFavorites = favorites;
        favoriteAdapter = new FavoriteAdapter(this, favorites);
        recyclerView.setAdapter(favoriteAdapter);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof FavoriteAdapter.FavoriteViewHolder) {
            String name = localFavorites.get(viewHolder.getAdapterPosition()).name;

            Favorite deleteItem = localFavorites.get(viewHolder.getAdapterPosition());
            int deleteIndex = viewHolder.getAdapterPosition();

            //delete item from adapter
            favoriteAdapter.removeItem(deleteIndex);

            //Delete item from Room Database
            Common.favoriteRepository.delete(deleteItem);

            Snackbar snackbar = Snackbar.make(rootLayout, new StringBuilder(name).append(" removed from Favorites List").toString(),
                    Snackbar.LENGTH_LONG);

            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favoriteAdapter.restoreItem(deleteItem, deleteIndex);
                    Common.favoriteRepository.insertFav(deleteItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }

    }
}