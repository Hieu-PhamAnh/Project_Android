package com.example.login.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.login.R;
import com.example.login.activity.user.ItemDetailActivity;
import com.example.login.activity.staff.AdminItemDetailActivity;
import com.example.login.model.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemRecyclerView extends RecyclerView.Adapter<ItemRecyclerView.ItemViewHolder> {

    private List<Item> list;
    private Activity activity;
    public ItemRecyclerView(Activity activity) {
        list = new ArrayList<>();
        this.activity = activity;
    }

    public void setItem(List<Item> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item o = list.get(position);
        holder.name.setText(o.getName());
        holder.price.setText(o.getPrice()+"$");
        holder.brand.setText(o.getBrand());
        Picasso.get().load(o.getImg()).into(holder.imgItem);
        Activity activity = this.activity;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(activity.getSharedPreferences("app",MODE_PRIVATE).getBoolean("admin",true)){
                    intent = new Intent(activity, AdminItemDetailActivity.class);
                    intent.putExtra("item", o.getId());
                }
                else{
                    intent = new Intent(activity, ItemDetailActivity.class);
                    intent.putExtra("item", o.getId());
                }

                activity.startActivity(intent);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        else
            return 0;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView name, price, brand;
        private ImageView imgItem;

        public ItemViewHolder(@NonNull View v) {
            super(v);
            name = v.findViewById(R.id.item_name);
            price = v.findViewById(R.id.item_price);
            brand = v.findViewById(R.id.item_brand);
            imgItem = v.findViewById(R.id.item_img);

        }
    }
}
