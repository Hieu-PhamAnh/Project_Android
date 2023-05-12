package com.example.login.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.adapter.OrderItemRecyclerView;
import com.example.login.adapter.OrderRecyclerView;
import com.example.login.api.ApiService;
import com.example.login.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WatchOderActivity extends AppCompatActivity {
    private RecyclerView list_item;
    private OrderRecyclerView orderadapter;
    private TextView home,logout;

    FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    ArrayList<Order> i=new ArrayList<>();
    long s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watchorder_activity);
        SharedPreferences sharedPreferences1=WatchOderActivity.this.getSharedPreferences("app",MODE_PRIVATE);
        SharedPreferences.Editor editor1= sharedPreferences1.edit();
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        home=findViewById(R.id.bhome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WatchOderActivity.this, MainActivity.class));
                WatchOderActivity.this.finish();
            }
        });
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor1.putString("user","");
                editor1.commit();
                startActivity(new Intent(WatchOderActivity.this, LoginOptionActivity.class));
                WatchOderActivity.this.finish();
            }
        });
        orderadapter = new OrderRecyclerView(this);
        list_item=findViewById(R.id.orderlist);
        list_item.setLayoutManager(new GridLayoutManager(this,1));

        reference = rootNode.getReference("order");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                s=0;
                i.clear();
                ApiService.apiService.getOrderList().enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try {
                            JSONObject object = new JSONObject(response.body().toString());
                            Iterator<String> stringIterator = object.keys();
                            while (stringIterator.hasNext()) {
                                //namesList.add(stringIterator.next());
                                ApiService.apiService.getOrder(stringIterator.next()).enqueue(new Callback<Order>() {
                                    @Override
                                    public void onResponse(Call<Order> call, Response<Order> response) {
                                        if(response.code()==200){
                                            Order u=response.body();

                                            if(type.equals("")&&u.getUserId().equalsIgnoreCase(sharedPreferences1.getString("user",""))){
                                                i.add(u);
                                                orderadapter.setItem(i);
                                                list_item.setAdapter(orderadapter);
                                            }
                                            if(type.equals("import")&&u.getType().equalsIgnoreCase("import")){
                                                i.add(u);
                                                orderadapter.setItem(i);
                                                list_item.setAdapter(orderadapter);
                                            }
                                            if(type.equals("buy")&&u.getType().equalsIgnoreCase("buy")){
                                                i.add(u);
                                                orderadapter.setItem(i);
                                                list_item.setAdapter(orderadapter);
                                            }

                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<Order> call, Throwable t) {
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




    }
}