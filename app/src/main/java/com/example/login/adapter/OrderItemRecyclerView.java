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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderItemRecyclerView extends RecyclerView.Adapter<OrderItemRecyclerView.ItemViewHolder> {

    private List<OrderItem> list;
    private Activity activity;
    OrderItem o;
    Item i;
    public OrderItemRecyclerView(Activity activity) {
        list = new ArrayList<>();
        this.activity = activity;
    }

    public void setItem(List<OrderItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public OrderItemRecyclerView.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.orderitem, parent, false);
        return new OrderItemRecyclerView.ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemRecyclerView.ItemViewHolder holder, int position) {
        o = list.get(position);
        holder.price.setText(o.getItemprice()+"$");
        holder.quantity.setText(o.getQuantity()+"");
        holder.tang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ApiService.apiService.editOrderItemQuantity(list.get(position).getId(),list.get(position).getQuantity()+1).enqueue(new Callback<Long>() {
                    @Override
                    public void onResponse(Call<Long> call, Response<Long> response) {
                    }

                    @Override
                    public void onFailure(Call<Long> call, Throwable t) { }
                });
            }
        });
        holder.xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiService.apiService.deleteOrderItem(list.get(position).getId()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
        holder.giam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.get(position).getQuantity()>1){
                    list.get(position).setQuantity(list.get(position).getQuantity()-1);
                    ApiService.apiService.editOrderItem(list.get(position).getId(),list.get(position)).enqueue(new Callback<OrderItem>() {
                        @Override
                        public void onResponse(Call<OrderItem> call, Response<OrderItem> response) {

                        }

                        @Override
                        public void onFailure(Call<OrderItem> call, Throwable t) { }
                    });
                }
                else{
                    ApiService.apiService.deleteOrderItem(list.get(position).getId()).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            }
        });
        holder.quantity.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                    if(Long.parseLong(holder.quantity.getText().toString())==0){
                        ApiService.apiService.deleteOrderItem(list.get(position).getId()).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });

                    }
                    else{
                        list.get(position).setQuantity(Long.parseLong(holder.quantity.getText().toString()));
                        ApiService.apiService.editOrderItem(list.get(position).getId(),list.get(position)).enqueue(new Callback<OrderItem>() {
                            @Override
                            public void onResponse(Call<OrderItem> call, Response<OrderItem> response) {
                            }

                            @Override
                            public void onFailure(Call<OrderItem> call, Throwable t) { }
                        });
                    }
                    return true;
                }
                return false;
            }
        });
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
        private TextView name, price, quantity,giam,tang,xoa;
        private ImageView imgItem;

        public ItemViewHolder(@NonNull View v) {
            super(v);
            name = v.findViewById(R.id.cart_name);
            price = v.findViewById(R.id.cart_price);
            imgItem = v.findViewById(R.id.cart_img);
            quantity= v.findViewById(R.id.soluong);
            giam=v.findViewById(R.id.giam_sl);
            tang=v.findViewById(R.id.tang_sl);
            xoa=v.findViewById(R.id.cancel_order);
        }
    }
}

