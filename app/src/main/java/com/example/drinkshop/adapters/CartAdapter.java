package com.example.drinkshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.drinkshop.R;
import com.example.drinkshop.database.modeldb.Cart;
import com.example.drinkshop.database.modeldb.Favorite;
import com.example.drinkshop.utils.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    Context context;
    List<Cart> carts;

    public CartAdapter(Context context, List<Cart> carts) {
        this.context = context;
        this.carts = carts;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Picasso.with(context)
                .load(carts.get(position).link)
                .into(holder.imageView);

        holder.txt_amount.setNumber(String.valueOf(carts.get(position).amount));
        holder.txt_price.setText(new StringBuilder("$").append(carts.get(position).price));
        holder.txt_product_name.setText(new StringBuilder(carts.get(position).name).append(" x")
        .append(carts.get(position).amount)
                .append(carts.get(position).size ==0? " Size M":"Size L"));
        holder.txt_sugar_ice.setText(new StringBuilder("Sugar: ")
        .append(carts.get(position).sugar).append("%").append("\n")
        .append("Ice: ").append(carts.get(position).ice).append("%").toString());

        //get Price of one cup with all options
        double priceOneCup = carts.get(position).price / carts.get(position).amount;

        //Auto Save item when user change amount
        holder.txt_amount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Cart cart = carts.get(position);
                cart.amount = newValue;
                cart.price = Math.round(priceOneCup*newValue);

                Common.cartRepository.updateCart(cart);

                holder.txt_price.setText(new StringBuilder("$").append(carts.get(position).price));
            }
        });

    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView txt_product_name, txt_sugar_ice, txt_price;
        ElegantNumberButton txt_amount;
        public ConstraintLayout view_background;
        public LinearLayout view_foreground;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.img_product);
            txt_amount=itemView.findViewById(R.id.txt_amount);
            txt_product_name=itemView.findViewById(R.id.text_product_name);
            txt_sugar_ice=itemView.findViewById(R.id.text_sugar_ice);
            txt_price=itemView.findViewById(R.id.text_price);
            view_background=itemView.findViewById(R.id.view_background);
            view_foreground=itemView.findViewById(R.id.view_foreground);
        }
    }

    public void removeItem(int position){
        carts.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Cart item, int position){
        carts.add(position,item);
        notifyItemInserted(position);
    }
}
