package com.example.login.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login.R;
import com.example.login.api.ApiService;
import com.example.login.model.Item;
import com.example.login.model.OrderItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailActivity extends AppCompatActivity {
    private TextView name, brand,price, addoi,home,cart,logout;
    private ImageView img;
    private Item i=new Item();
    private OrderItem o;
    int c=0;

    FirebaseDatabase rootNode = FirebaseDatabase.getInstance();

    DatabaseReference reference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String id = intent.getStringExtra("item");
        setContentView(R.layout.itemdetail_activity);
        SharedPreferences sharedPreferences1=ItemDetailActivity.this.getSharedPreferences("app",MODE_PRIVATE);
        String user = sharedPreferences1.getString("user", "");
        SharedPreferences.Editor editor1= sharedPreferences1.edit();
        name=findViewById(R.id.dname);
        brand=findViewById(R.id.dbrand);
        price=findViewById(R.id.dprice);
        addoi= findViewById(R.id.addoitem);
        img=findViewById(R.id.iimg);
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor1.putString("user","");
                editor1.commit();
                startActivity(new Intent(ItemDetailActivity.this, LoginOptionActivity.class));
                ItemDetailActivity.this.finish();
            }
        });
        o=null;
        c=0;
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
                                    if(u.getItemId().equalsIgnoreCase(getIntent().getStringExtra("item"))
                                            &&!u.getOrdered()&&u.getUserId().equalsIgnoreCase(user)){
                                        c++;
                                        o=u;
                                        o.setQuantity(o.getQuantity()+1);

                                    }
                                    if(!stringIterator.hasNext()) {
                                        if(o==null){
                                            c++;
                                            o = new OrderItem();
                                            o.setUserId(sharedPreferences1.getString("user", ""));
                                            o.setItemId(getIntent().getStringExtra("item"));
                                            if (sharedPreferences1.getBoolean("admin", true))
                                                o.setItemprice(i.getImprice());
                                            else o.setItemprice(i.getPrice());
                                            o.setOrdered(false);
                                            o.setQuantity(1);
                                            reference = rootNode.getReference("orderitem");
                                            String t = reference.push().getKey();
                                            o.setId(t);
                                        }
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
                o = new OrderItem();
                o.setUserId(user);
                o.setItemId(getIntent().getStringExtra("item"));
                if (sharedPreferences1.getBoolean("admin", true))
                    o.setItemprice(i.getImprice());
                else o.setItemprice(i.getPrice());
                o.setOrdered(false);
                o.setQuantity(1);
                reference = rootNode.getReference("orderitem");
                String t = reference.push().getKey();
                o.setId(t);
            }
        });
        cart=findViewById(R.id.cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ItemDetailActivity.this, OrderActivity.class));
                ItemDetailActivity.this.finish();
            }
        });
        home=findViewById(R.id.bhome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ItemDetailActivity.this, MainActivity.class));
                ItemDetailActivity.this.finish();
            }
        });
        addoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiService.apiService.editOrderItem(o.getId(),o).enqueue(new Callback<OrderItem>() {
                    @Override
                    public void onResponse(Call<OrderItem> call, Response<OrderItem> response) {
                        startActivity(new Intent(ItemDetailActivity.this, MainActivity.class));
                        ItemDetailActivity.this.finish();
                    }

                    @Override
                    public void onFailure(Call<OrderItem> call, Throwable t) { }
                });
            }
        });
        ApiService.apiService.getItem(id).enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if(response.code()==200){
                    i=response.body();
                    name.setText(i.getName());
                    brand.setText("Hãng "+i.getBrand());
                    price.setText("Giá " +i.getPrice()+"$");
                    Picasso.get().load(i.getImg()).into(img);
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
            }
        });
    }

}
