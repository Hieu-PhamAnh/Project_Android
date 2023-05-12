package com.example.login.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.activity.staff.AdminItemDetailActivity;
import com.example.login.activity.staff.EditOrderActivity;
import com.example.login.activity.user.ItemDetailActivity;
import com.example.login.api.ApiService;
import com.example.login.model.Order;
import com.example.login.model.OrderItem;
import com.example.login.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRecyclerView extends RecyclerView.Adapter<OrderRecyclerView.ItemViewHolder> {
    private List<Order> list;
    private Activity activity;
    private OderedOrderItemRecyclerView itemadapter;

    ArrayList<OrderItem> i=new ArrayList<>();
    Order o;
    public OrderRecyclerView(Activity activity) {
        list = new ArrayList<>();
        this.activity = activity;

    }

    public void setItem(List<Order> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public OrderRecyclerView.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.order, parent, false);
        return new OrderRecyclerView.ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderRecyclerView.ItemViewHolder holder, int position) {
        o = list.get(position);
        holder.price.setText(o.getSum()+"$");
        holder.date.setText(o.getDate());
        if(o.getReceived()) holder.status.setText(("Trạng thái: Đã nhận"));
        if(o.getShipping()) holder.status.setText(("Trạng thái: Đang giao"));
        if(o.getWaiting()) holder.status.setText(("Trạng thái: Đang chờ"));
        ApiService.apiService.getUser(list.get(position).getUserId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code()==200){
                    User u=response.body();
                    holder.name.setText(u.getName());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
        if(activity.getSharedPreferences("app",MODE_PRIVATE).getBoolean("admin", false)){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(activity, EditOrderActivity.class);
                        intent.putExtra("order", o.getId());



                    activity.startActivity(intent);
                    activity.finish();
                }
            });
        }



    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        else
            return 0;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView name, price, date,status;

        public ItemViewHolder(@NonNull View v) {
            super(v);
            name = v.findViewById(R.id.oname);
            date = v.findViewById(R.id.odate);
            price = v.findViewById(R.id.oprice);
            status=v.findViewById(R.id.ostatus);

        }
    }
}
