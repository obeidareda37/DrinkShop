package com.example.drinkshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drinkshop.R;
import com.example.drinkshop.interfaces.ItemClickListener;
import com.example.drinkshop.model.Category;
import com.example.drinkshop.ui.DrinkActivity;
import com.example.drinkshop.utils.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    Context context;
    List<Category> categories;


    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(context).inflate(R.layout.menu_item_layout,parent,false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        //load Image

        Picasso.with(context)
                .load(categories.get(position).Link)
                .into(holder.image_product);

        holder.textMenuName.setText(categories.get(position).Name);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v) {
                Common.currentCategory=categories.get(position);

                context.startActivity(new Intent(context, DrinkActivity.class));
            }
        });


    }

    @Override
    public int getItemCount() {
        return categories.size();
    }



    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView image_product;
        TextView textMenuName;
        ItemClickListener itemClickListener;



        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            image_product=itemView.findViewById(R.id.image_product);
            textMenuName=itemView.findViewById(R.id.text_menu_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v);
        }
    }

}
