package com.example.login.adapter;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.api.ApiService;
import com.example.login.model.Item;
import com.example.login.model.OrderItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OderedOrderItemRecyclerView extends RecyclerView.Adapter<OderedOrderItemRecyclerView.ItemViewHolder> {
    private List<OrderItem> list;
    private Activity activity;
    OrderItem o;
    Item i;
    public OderedOrderItemRecyclerView(Activity activity) {
        list = new ArrayList<>();
        this.activity = activity;
    }

    public void setItem(List<OrderItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public OderedOrderItemRecyclerView.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.orderedorderitem, parent, false);
        return new OderedOrderItemRecyclerView.ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OderedOrderItemRecyclerView.ItemViewHolder holder, int position) {
        o = list.get(position);
        holder.price.setText(o.getItemprice()+"$");
        holder.quantity.setText(o.getQuantity()+"");
        ApiService.apiService.getItem(o.getItemId()).enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if(response.code()==200){
                    i=response.body();

                    holder.name.setText(i.getName());
                    Picasso.get().load(i.getImg()).into(holder.imgItem);
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
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
        private TextView name, price, quantity;
        private ImageView imgItem;

        public ItemViewHolder(@NonNull View v) {
            super(v);
            name = v.findViewById(R.id.cart_name);
            price = v.findViewById(R.id.cart_price);
            imgItem = v.findViewById(R.id.cart_img);
            quantity= v.findViewById(R.id.soluong);

        }
    }
}
