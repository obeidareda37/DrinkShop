package com.example.drinkshop.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.drinkshop.R;
import com.example.drinkshop.adapters.DrinkAdapter;
import com.example.drinkshop.model.Category;
import com.example.drinkshop.model.Drink;
import com.example.drinkshop.network.IDrinkShopAPI;
import com.example.drinkshop.utils.Common;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class DrinkActivity extends AppCompatActivity {

    IDrinkShopAPI mService;
    RecyclerView recyclerView;
    TextView txt_banner_name;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);
        recyclerView=findViewById(R.id.recycler_drinks);
        mService = Common.getAPI();

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

        txt_banner_name = findViewById(R.id.txt_menu_name);
        txt_banner_name.setText(Common.currentCategory.Name);

        loadListDrink(Common.currentCategory.ID);
    }

    private void loadListDrink(String menuId) {
        compositeDisposable.add(mService.getDrink(menuId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Drink>>() {

                    @Override
                    public void accept(List<Drink> drinks) throws Exception {
                        displayDrinkList(drinks);

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

    private void displayDrinkList(List<Drink> drinks) {

        DrinkAdapter adapter = new DrinkAdapter(this,drinks);
        recyclerView.setAdapter(adapter);
    }


}