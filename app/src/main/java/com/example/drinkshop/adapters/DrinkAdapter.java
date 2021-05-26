package com.example.drinkshop.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.drinkshop.R;
import com.example.drinkshop.database.modeldb.Cart;
import com.example.drinkshop.database.modeldb.Favorite;
import com.example.drinkshop.interfaces.ItemClickListener;
import com.example.drinkshop.model.Drink;
import com.example.drinkshop.utils.Common;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder> {

    Context context;
    List<Drink> drinkList;

    public DrinkAdapter(Context context, List<Drink> drinkList) {
        this.context = context;
        this.drinkList = drinkList;
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.drink_item_layout, parent, false);
        return new DrinkViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkViewHolder holder, int position) {


        holder.btn_add_to_cart.setOnClickListener(v -> showAddToCartDialog(position));

        holder.txt_price.setText(new StringBuilder("$").append(drinkList.get(position).Price).toString());
        holder.txt_drink_name.setText(drinkList.get(position).Name);
        Picasso.with(context).load(drinkList.get(position).Link).into(holder.imageProduct);
        holder.setItemClickListener(v -> Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show());


        //favorite system
        if (Common.favoriteRepository.isFavorite(Integer.parseInt(drinkList.get(position).ID)) == 1) {
            holder.btn_favorite.setImageResource(R.drawable.ic_favorite);

        } else {
            holder.btn_favorite.setImageResource(R.drawable.ic_favorite_border);
        }

        holder.btn_favorite.setOnClickListener(v -> {
            if (Common.favoriteRepository.isFavorite(Integer.parseInt(drinkList.get(position).ID)) != 1) {

                addOrRemoveFavorite(drinkList.get(position), true);
                holder.btn_favorite.setImageResource(R.drawable.ic_favorite);
            } else {
                addOrRemoveFavorite(drinkList.get(position), false);
                holder.btn_favorite.setImageResource(R.drawable.ic_favorite_border);
            }
        });

    }


    private void addOrRemoveFavorite(Drink drink, boolean isAdd) {
        Favorite favorite = new Favorite();
        favorite.id = drink.ID;
        favorite.name = drink.Name;
        favorite.link = drink.Link;
        favorite.price = drink.Price;
        favorite.menuId = drink.MenuId;
        Log.d("AddFav", favorite.menuId);

        if (isAdd) {
            Common.favoriteRepository.insertFav(favorite);
        } else {
            Common.favoriteRepository.delete(favorite);
        }

    }


    private void showAddToCartDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.add_to_cart, null);

        ImageView image_product_dialog = itemView.findViewById(R.id.image_cart_product);
        ElegantNumberButton text_count = itemView.findViewById(R.id.text_count);
        TextView text_product_dialog = itemView.findViewById(R.id.txt_cart_product_name);

        EditText edt_comment = itemView.findViewById(R.id.edt_comment);

        RadioButton rdi_sizeM = itemView.findViewById(R.id.rdi_sizeM);
        RadioButton rdi_sizeL = itemView.findViewById(R.id.rdi_sizeL);

        rdi_sizeM.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                Common.sizeOfCup = 0;
        });

        rdi_sizeL.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                Common.sizeOfCup = 1;
        });


        RadioButton rdi_sugar_100 = itemView.findViewById(R.id.rdi_sugar_100);
        RadioButton rdi_sugar_70 = itemView.findViewById(R.id.rdi_sugar_70);
        RadioButton rdi_sugar_50 = itemView.findViewById(R.id.rdi_sugar_50);
        RadioButton rdi_sugar_30 = itemView.findViewById(R.id.rdi_sugar_30);
        RadioButton rdi_sugar_free = itemView.findViewById(R.id.rdi_sugar_free);

        rdi_sugar_30.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                Common.sugar = 30;
        });

        rdi_sugar_50.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                Common.sugar = 50;
        });

        rdi_sugar_70.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                Common.sugar = 70;
        });

        rdi_sugar_100.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                Common.sugar = 100;
        });

        rdi_sugar_free.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                Common.sugar = 0;
        });


        RadioButton rdi_ice_100 = itemView.findViewById(R.id.rdi_ice_100);
        RadioButton rdi_ice_70 = itemView.findViewById(R.id.rdi_ice_70);
        RadioButton rdi_ice_50 = itemView.findViewById(R.id.rdi_ice_50);
        RadioButton rdi_ice_30 = itemView.findViewById(R.id.rdi_ice_30);
        RadioButton rdi_ice_free = itemView.findViewById(R.id.rdi_ice_free);

        rdi_ice_30.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                Common.ice = 30;
        });

        rdi_ice_50.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                Common.ice = 50;
        });

        rdi_ice_70.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                Common.ice = 70;
        });

        rdi_ice_100.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                Common.ice = 100;
        });

        rdi_ice_free.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                Common.ice = 0;
        });


        RecyclerView recyclerView_topping = itemView.findViewById(R.id.recycler_topping);
        recyclerView_topping.setLayoutManager(new LinearLayoutManager(context));
        recyclerView_topping.setHasFixedSize(true);

        MultiChoiceAdapter adapter = new MultiChoiceAdapter(context, Common.toppingList);
        recyclerView_topping.setAdapter(adapter);


        Picasso.with(context).load(drinkList.get(position).Link)
                .into(image_product_dialog);

        text_product_dialog.setText(drinkList.get(position).Name);

        builder.setView(itemView);
        builder.setNegativeButton("ADD TO CART", (dialog, which) -> {
            if (Common.sizeOfCup == -1) {
                Toast.makeText(context, "Please choose size of cup", Toast.LENGTH_SHORT).show();
                return;
            }

            if (Common.sugar == -1) {
                Toast.makeText(context, "Please choose sugar", Toast.LENGTH_SHORT).show();
                return;


            }

            if (Common.ice == -1) {
                Toast.makeText(context, "Please choose ice", Toast.LENGTH_SHORT).show();
                return;

            }
            showConfirmDialog(position, text_count.getNumber());
            dialog.dismiss();
        });

        builder.show();
    }

    private void showConfirmDialog(int position, String number) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View itemView1 = LayoutInflater.from(context)
                .inflate(R.layout.confirm_add_to_cart_layout, null);
        ImageView image_product_dialog = itemView1.findViewById(R.id.img_product1);
        TextView text_product_dialog = itemView1.findViewById(R.id.txt_cart_product_name1);
        TextView text_product_price = itemView1.findViewById(R.id.txt_cart_product_price1);
        TextView text_sugar = itemView1.findViewById(R.id.txt_sugar);
        TextView text_ice = itemView1.findViewById(R.id.txt_ice);
        TextView text_topping_extra = itemView1.findViewById(R.id.txt_topping_extra);


        Picasso.with(context).load(drinkList.get(position).Link).into(image_product_dialog);
        text_product_dialog.setText(new StringBuilder(drinkList.get(position).Name).append(" x")
                .append(Common.sizeOfCup == 0 ? " Size M" : " Size L")
                .append(number).toString());

        text_ice.setText(new StringBuilder("Ice: ").append(Common.ice).append("%").toString());
        text_sugar.setText(new StringBuilder("Sugar: ").append(Common.sugar).append("%").toString());

        double price = (Double.parseDouble(drinkList.get(position).Price)) * Double.parseDouble(number) + Common.toppingPrice;
        if (Common.sizeOfCup == 1)
            price += (3.0 * Double.parseDouble(number));


        StringBuilder topping_final_comment = new StringBuilder("");
        for (String line : Common.toppingAdded)
            topping_final_comment.append(line).append("\n");

        text_topping_extra.setText(topping_final_comment);

        final double finalPrice = Math.round(price);

        text_product_price.setText(new StringBuilder("$").append(finalPrice));


        builder.setNegativeButton("CONFIRM", (dialog, which) -> {

            //Add to database
            dialog.dismiss();
            try {
                Cart cartItem = new Cart();
                cartItem.name = drinkList.get(position).Name;
                cartItem.amount = Integer.parseInt(number);
                cartItem.ice = Common.ice;
                cartItem.sugar = Common.sugar;
                cartItem.price = finalPrice;
                cartItem.size = Common.sizeOfCup;
                cartItem.toppingExtras = text_topping_extra.getText().toString();
                cartItem.link = drinkList.get(position).Link;

                Common.cartRepository.insertToCart(cartItem);

                Log.d("ShopDrink", new Gson().toJson(cartItem));

                Toast.makeText(context, "Save item to cart success", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setView(itemView1);
        builder.show();
    }


    @Override
    public int getItemCount() {
        return drinkList.size();
    }

    class DrinkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageProduct;
        TextView txt_drink_name, txt_price;
        ItemClickListener itemClickListener;
        ImageView btn_add_to_cart, btn_favorite;

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }


        public DrinkViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.image_product);
            txt_drink_name = itemView.findViewById(R.id.text_drink_name);
            txt_price = itemView.findViewById(R.id.txt_price);
            btn_add_to_cart = itemView.findViewById(R.id.btn_add_cart);
            btn_favorite = itemView.findViewById(R.id.btn_favorite);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v);
        }
    }
}
