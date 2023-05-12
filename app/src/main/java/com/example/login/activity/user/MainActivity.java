package com.example.login.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.activity.staff.AdminFunctionActivity;
import com.example.login.adapter.ItemRecyclerView;
import com.example.login.api.ApiService;
import com.example.login.model.Item;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private RecyclerView list_item;
    private ItemRecyclerView itemadapter;
    private TextView find,all,cart,logout,home;
    private EditText fname;

    ArrayList<Item> i=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences1=MainActivity.this.getSharedPreferences("app",MODE_PRIVATE);
        SharedPreferences.Editor editor1= sharedPreferences1.edit();
        itemadapter = new ItemRecyclerView(this);
        list_item=findViewById(R.id.iitem);
        list_item.setLayoutManager(new GridLayoutManager(this,2));
        find=findViewById(R.id.fname);
        fname=findViewById(R.id.snam);
        all=findViewById(R.id.itema);
        home=findViewById(R.id.bhome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sharedPreferences1.getBoolean("admin",true)){
                    startActivity(new Intent(MainActivity.this, AdminFunctionActivity.class));
                    MainActivity.this.finish();
                }
            }
        });

        cart=findViewById(R.id.cart);
        if(sharedPreferences1.getBoolean("admin",true)) cart.setText("Giỏ hàng nhập");
        else cart.setText("Giỏ hàng");
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, OrderActivity.class));
                MainActivity.this.finish();
            }
        });
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor1.putString("user","");
                editor1.commit();
                startActivity(new Intent(MainActivity.this, LoginOptionActivity.class));
                MainActivity.this.finish();
            }
        });
        i.clear();
        ApiService.apiService.getItemList().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject object = new JSONObject(response.body().toString());
                    Iterator<String> stringIterator = object.keys();
                    while (stringIterator.hasNext()) {
                        //namesList.add(stringIterator.next());
                        ApiService.apiService.getItem(stringIterator.next()).enqueue(new Callback<Item>() {
                            @Override
                            public void onResponse(Call<Item> call, Response<Item> response) {
                                if(response.code()==200){
                                    Item item=response.body();
                                    i.add(item);
                                    itemadapter.setItem(i);
                                    list_item.setAdapter(itemadapter);
                                }
                            }

                            @Override
                            public void onFailure(Call<Item> call, Throwable t) {
                            }
                        });

                    }
                    if(!stringIterator.hasNext()){

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i.clear();
                ApiService.apiService.getItemList().enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try {
                            JSONObject object = new JSONObject(response.body().toString());
                            Iterator<String> stringIterator = object.keys();
                            while (stringIterator.hasNext()) {
                                //namesList.add(stringIterator.next());
                                ApiService.apiService.getItem(stringIterator.next()).enqueue(new Callback<Item>() {
                                    @Override
                                    public void onResponse(Call<Item> call, Response<Item> response) {
                                        if(response.code()==200){
                                            Item item=response.body();
                                            i.add(item);
                                            itemadapter.setItem(i);
                                            list_item.setAdapter(itemadapter);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Item> call, Throwable t) {
                                    }
                                });

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                    }
                });
            }
        });
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i.clear();
                ApiService.apiService.getItemList().enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try {
                            JSONObject object = new JSONObject(response.body().toString());
                            Iterator<String> stringIterator = object.keys();
                            while (stringIterator.hasNext()) {
                                //namesList.add(stringIterator.next());
                                ApiService.apiService.getItem(stringIterator.next()).enqueue(new Callback<Item>() {
                                    @Override
                                    public void onResponse(Call<Item> call, Response<Item> response) {
                                        if(response.code()==200){
                                            Item item=response.body();
                                            if(item.getName().equalsIgnoreCase(fname.getText().toString())){
                                                i.add(item);
                                                itemadapter.setItem(i);
                                                list_item.setAdapter(itemadapter);
                                            }

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Item> call, Throwable t) {
                                    }
                                });

                            }
                            if(!stringIterator.hasNext()){
                                itemadapter.setItem(i);
                                list_item.setAdapter(itemadapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                    }
                });
            }
        });

    }
}
