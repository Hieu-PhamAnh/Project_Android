package com.example.login.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.adapter.OrderItemRecyclerView;
import com.example.login.api.ApiService;
import com.example.login.model.Order;
import com.example.login.model.OrderItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderActivity extends AppCompatActivity {
    private RecyclerView list_item;
    private OrderItemRecyclerView itemadapter;
    private EditText phone;
    private TextView order,home,logout,sum,invoice,address;

    FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    ArrayList<OrderItem> i=new ArrayList<>();
    long s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cart_activity);
        SharedPreferences sharedPreferences1=OrderActivity.this.getSharedPreferences("app",MODE_PRIVATE);
        SharedPreferences.Editor editor1= sharedPreferences1.edit();
        phone=findViewById(R.id.phone);
        address=findViewById(R.id.address);
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderActivity.this, MapActivity.class));
                OrderActivity.this.finish();
            }
        });
        String add=sharedPreferences1.getString("address","");
        if(!add.equalsIgnoreCase(""))
            address.setText(add);
        sum=findViewById(R.id.sum);
        home=findViewById(R.id.bhome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderActivity.this, MainActivity.class));
                OrderActivity.this.finish();
            }
        });
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor1.putString("user","");
                editor1.commit();
                startActivity(new Intent(OrderActivity.this, LoginOptionActivity.class));
                OrderActivity.this.finish();
            }
        });
        itemadapter = new OrderItemRecyclerView(this);
        list_item=findViewById(R.id.oitem);
        list_item.setLayoutManager(new GridLayoutManager(this,1));

        reference = rootNode.getReference("orderitem");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                s=0;
                i.clear();
                itemadapter.setItem(i);
                list_item.setAdapter(itemadapter);
                sum.setText("$"+s);
                ApiService.apiService.getOrderItemList().enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try {
                            JSONObject object = new JSONObject(response.body().toString());
                            Iterator<String> stringIterator = object.keys();
                            while (stringIterator.hasNext()) {
                                //namesList.add(stringIterator.next());
                                ApiService.apiService.getOrderItem(stringIterator.next()).enqueue(new Callback<OrderItem>() {
                                    @Override
                                    public void onResponse(Call<OrderItem> call, Response<OrderItem> response) {
                                        if(response.code()==200){
                                            OrderItem u=response.body();
                                            if(u.getUserId().equalsIgnoreCase(sharedPreferences1.getString("user",""))
                                                    &&!u.getOrdered()){
                                                i.add(u);
                                                s+=u.getQuantity()*u.getItemprice();
                                                itemadapter.setItem(i);
                                                list_item.setAdapter(itemadapter);
                                                sum.setText("$"+s);
                                            }
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<OrderItem> call, Throwable t) {
                                    }
                                });

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable a) {
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        order=findViewById(R.id.payment);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order o= new Order();
                o.setAddress(address.getText().toString());
                o.setPhone(phone.getText().toString());
                o.setSum(s);
                o.setWaiting(true);
                o.setShipping(false);
                o.setReceived(false);
                if(OrderActivity.this.getSharedPreferences("app",MODE_PRIVATE).getBoolean("admin", true))
                    o.setType("import");
                else o.setType("buy");
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                o.setDate(formatter.format(date));
                o.setUserId(OrderActivity.this.getSharedPreferences("app",MODE_PRIVATE).getString("user", ""));
                FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
                DatabaseReference reference = rootNode.getReference("order");
                String t= reference.child("order").push().getKey();
                o.setId(t);
                ApiService.apiService.editOrder(o.getId(),o).enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        for(OrderItem oi:i){
                            oi.setOrdered(true);
                            oi.setOrderId(t);
                            ApiService.apiService.editOrderItem(oi.getId(),oi).enqueue(new Callback<OrderItem>() {
                                @Override
                                public void onResponse(Call<OrderItem> call, Response<OrderItem> response) {
                                }
                                @Override
                                public void onFailure(Call<OrderItem> call, Throwable t) {
                                }
                            });

                        }
                        startActivity(new Intent(OrderActivity.this, MainActivity.class));
                        OrderActivity.this.finish();
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {

                    }
                });
            }
        });
        invoice=findViewById(R.id.invoice);
        invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(OrderActivity.this, WatchOderActivity.class);
                intent.putExtra("type", "");
                startActivity(intent);
                OrderActivity.this.finish();
            }
        });


    }
}
