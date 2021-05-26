package com.example.drinkshop.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.drinkshop.R;
import com.example.drinkshop.adapters.CartAdapter;
import com.example.drinkshop.adapters.FavoriteAdapter;
import com.example.drinkshop.database.modeldb.Cart;
import com.example.drinkshop.database.modeldb.Favorite;
import com.example.drinkshop.model.Order;
import com.example.drinkshop.network.IDrinkShopAPI;
import com.example.drinkshop.utils.Common;
import com.example.drinkshop.utils.RecyclerItemTouchHelper;
import com.example.drinkshop.utils.RecyclerItemTouchHelperListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    RecyclerView recyclerView;
    Button btn_place_order;
    CompositeDisposable compositeDisposable;
    List<Cart> cartList = new ArrayList<>();
    CartAdapter adapter;
    ConstraintLayout rootLayout;
    IDrinkShopAPI mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        compositeDisposable = new CompositeDisposable();
        mService=Common.getAPI();
        recyclerView = findViewById(R.id.recycler_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        rootLayout = findViewById(R.id.rootLayout);
        btn_place_order = findViewById(R.id.btn_place_order);
        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });
        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        loadCartItem();


    }

    private void placeOrder() {
        //Create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Submit Order");
        View submit_order_layout = LayoutInflater.from(this).inflate(R.layout.submit_order_layout,null);
        EditText ed_comment = submit_order_layout.findViewById(R.id.edt_comment);
        EditText ed_Address = submit_order_layout.findViewById(R.id.edt_address);
        EditText ed_Phone = submit_order_layout.findViewById(R.id.edt_phone);


        builder.setView(submit_order_layout);
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String orderComment = ed_comment.getText().toString();
                String orderAddress = ed_Address.getText().toString();
                String orderPhone = ed_Phone.getText().toString();

                //submit Order
                compositeDisposable.add(
                        Common.cartRepository.getCartItems()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<List<Cart>>() {
                            @Override
                            public void accept(List<Cart> carts) throws Exception {
                                    if (!TextUtils.isEmpty(orderAddress) || !TextUtils.isEmpty(orderPhone)){

                                        sendOrderToServer(Common.cartRepository.sumPrice(),carts,orderComment,orderAddress,orderPhone);
                                    }else{
                                        Toast.makeText(CartActivity.this, "Order Address and Phone number can't null", Toast.LENGTH_SHORT).show();
                                    }
                            }
                        })
                );

            }
        });

        builder.show();
    }

    private void sendOrderToServer(float sumPrice, List<Cart> carts, String orderComment, String orderAddress, String orderPhone) {
        if (carts.size()>0){
            String orderDetail = new Gson().toJson(carts);
            mService.submitOrder(sumPrice,orderDetail,orderAddress,orderPhone,orderComment).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Toast.makeText(CartActivity.this, "Order submit", Toast.LENGTH_SHORT).show();

                    //empty Cart
                    Common.cartRepository.emptyCart();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("ERROR",t.getMessage());
                }
            });
        }



    }


    private void loadCartItem() {

        compositeDisposable.add(
                Common.cartRepository.getCartItems()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<List<Cart>>() {
                            @Override
                            public void accept(List<Cart> carts) throws Exception {
                                displayCartItem(carts);
                            }
                        })
        );
    }

    private void displayCartItem(List<Cart> carts) {
        cartList = carts;
        adapter = new CartAdapter(this, carts);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCartItem();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartAdapter.CartViewHolder) {
            String name = cartList.get(viewHolder.getAdapterPosition()).name;

            final Cart deleteItem = cartList.get(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();

            //delete item from adapter
            adapter.removeItem(deleteIndex);

            //Delete item from Room Database
            Common.cartRepository.deleteCartItem(deleteItem);

            Snackbar snackbar = Snackbar.make(rootLayout, new StringBuilder(name).append(" removed from Cart List").toString(),
                    Snackbar.LENGTH_LONG);

            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.restoreItem(deleteItem, deleteIndex);
                    Common.cartRepository.insertToCart(deleteItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }

    }
}