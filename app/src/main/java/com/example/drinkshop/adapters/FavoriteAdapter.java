package com.example.drinkshop.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drinkshop.R;
import com.example.drinkshop.database.modeldb.Favorite;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    Context context;
    List<Favorite> favoriteList;

    public FavoriteAdapter(Context context, List<Favorite> favoriteList) {
        this.context = context;
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.favorite_item, parent, false);
        return new FavoriteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Picasso.with(context).load(favoriteList.get(position).link).into(holder.img_product);
        holder.txt_product_price.setText(new StringBuilder("$").append(favoriteList.get(position).price).toString());
        holder.txt_product_name.setText(favoriteList.get(position).name);

        Log.d("Dataleee",favoriteList.toString());

    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {

        ImageView img_product;
        TextView txt_product_name, txt_product_price;
        public ConstraintLayout view_background;
        public LinearLayout view_foreground;


        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);

            img_product = itemView.findViewById(R.id.img_product2);
            txt_product_name = itemView.findViewById(R.id.text_product_name2);
            txt_product_price = itemView.findViewById(R.id.text_price2);
            view_background=itemView.findViewById(R.id.view_background);
            view_foreground=itemView.findViewById(R.id.view_foreground);
        }
    }

    public void removeItem(int position){
        favoriteList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Favorite item,int position){
        favoriteList.add(position,item);
        notifyItemInserted(position);
    }
}
