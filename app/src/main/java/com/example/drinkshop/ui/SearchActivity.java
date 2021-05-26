package com.example.drinkshop.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.example.drinkshop.R;
import com.example.drinkshop.adapters.DrinkAdapter;
import com.example.drinkshop.model.Drink;
import com.example.drinkshop.network.IDrinkShopAPI;
import com.example.drinkshop.utils.Common;
import com.mancj.materialsearchbar.MaterialSearchBar;

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

public class SearchActivity extends AppCompatActivity {

    List<String> suggestList = new ArrayList<>();
    List<Drink>localDataSource = new ArrayList<>();
    MaterialSearchBar searchBar;

    IDrinkShopAPI mService;
    RecyclerView recyclerView;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    DrinkAdapter searchAdapter , adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //init service
        mService= Common.getAPI();
        recyclerView = findViewById(R.id.recycler_search);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        searchBar = findViewById(R.id.searchBar);
        searchBar.setHint("Enter your drink");

        loadAllDrinks();

        searchBar.setCardViewElevation(10);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                List<String> suggest = new ArrayList<>();
                for (String search:suggest){
                    if (search.toLowerCase().contains(searchBar.getText().toLowerCase())){
                        suggest.add(search);
                    }
                    searchBar.setLastSuggestions(suggest);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled){
                    recyclerView.setAdapter(adapter); // Restore full list drink
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                    startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {

        List<Drink> result = new ArrayList<>();
        for (Drink drink : localDataSource){
            if (drink.Name.contains(text)){
                result.add(drink);
            }

        }
        searchAdapter = new DrinkAdapter(this,result);
        recyclerView.setAdapter(searchAdapter);
    }

    private void loadAllDrinks() {
    compositeDisposable.add(mService.getAllDrinks().observeOn(AndroidSchedulers.mainThread())
    .subscribeOn(Schedulers.io())
    .subscribe(new Consumer<List<Drink>>() {
        @Override
        public void accept(List<Drink> drinks) throws Exception {
            displayListDrink(drinks);
            buildSuggestList(drinks);
        }
    }));


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
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    private void buildSuggestList(List<Drink> drinks) {
        for (Drink drink:drinks){
            suggestList.add(drink.Name);
        }
        searchBar.setLastSuggestions(suggestList);
    }

    private void displayListDrink(List<Drink> drinks) {
        localDataSource =drinks;
        adapter = new DrinkAdapter(this,drinks);
        recyclerView.setAdapter(adapter);
    }
}