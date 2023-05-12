package com.example.login.activity.staff;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.activity.user.LoginOptionActivity;
import com.example.login.activity.user.MainActivity;
import com.example.login.activity.user.OrderActivity;
import com.example.login.adapter.OderedOrderItemRecyclerView;
import com.example.login.adapter.OrderItemRecyclerView;
import com.example.login.api.ApiService;
import com.example.login.model.Item;
import com.example.login.model.Order;
import com.example.login.model.OrderItem;
import com.example.login.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditOrderActivity extends AppCompatActivity {
    private TextView edit,delete,home,cart,logout,name, phone, address,date, price;
    private ImageView img;
    private RadioButton wait,ship,rev;
    private Order i=new Order();
    ArrayList<OrderItem> oi=new ArrayList<>();
    int c=0;
    private RecyclerView list_item;
    private OderedOrderItemRecyclerView itemadapter;

    FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String id = intent.getStringExtra("order");
        setContentView(R.layout.editorder_activity);
        SharedPreferences sharedPreferences1= EditOrderActivity.this.getSharedPreferences("app",MODE_PRIVATE);
        String user = sharedPreferences1.getString("user", "");
        SharedPreferences.Editor editor1= sharedPreferences1.edit();
        name=findViewById(R.id.oname);
        phone=findViewById(R.id.ophone);
        date=findViewById(R.id.odate);
        address=findViewById(R.id.oaddress);
        price=findViewById(R.id.oprice);
        edit=findViewById(R.id.edititem);
        delete=findViewById(R.id.delitem);
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor1.putString("user","");
                editor1.commit();
                startActivity(new Intent(EditOrderActivity.this, LoginOptionActivity.class));
                EditOrderActivity.this.finish();
            }
        });
        ApiService.apiService.getOrder(id).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if(response.code()==200){
                    i=response.body();
                    phone.setText(i.getPhone());
                    date.setText(i.getDate());
                    address.setText(i.getAddress());
                    price.setText(i.getSum()+"");
                    ApiService.apiService.getUser(i.getUserId()).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if(response.code()==200){
                                User u=response.body();
                                name.setText(u.getName());

                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
            }
        });
        c=0;
        itemadapter = new OderedOrderItemRecyclerView(this);
        list_item=findViewById(R.id.oitem);
        list_item.setLayoutManager(new LinearLayoutManager(this));
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
                                    if(u.getOrderId()!=null&&u.getOrderId().equalsIgnoreCase(id)){
                                        oi.add(u);
                                        itemadapter.setItem(oi);
                                        list_item.setAdapter(itemadapter);
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

        cart=findViewById(R.id.imcart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditOrderActivity.this, OrderActivity.class));
                EditOrderActivity.this.finish();
            }
        });
        home=findViewById(R.id.bhome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditOrderActivity.this, MainActivity.class));
                EditOrderActivity.this.finish();
            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiService.apiService.editOrder(i.getId(),i).enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        Toast.makeText(EditOrderActivity.this, "Edit Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditOrderActivity.this, AdminFunctionActivity.class));
                        EditOrderActivity.this.finish();
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                    }
                });

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiService.apiService.deleteOrder(i.getId()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(EditOrderActivity.this, "Delete Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditOrderActivity.this, AdminFunctionActivity.class));
                        EditOrderActivity.this.finish();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
        wait=findViewById(R.id.owait);
        ship=findViewById(R.id.oship);
        rev=findViewById(R.id.orev);
        wait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i.setWaiting(true);
                i.setReceived(false);
                i.setShipping(false);
            }
        });
        ship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i.setWaiting(false);
                i.setReceived(false);
                i.setShipping(true);
            }
        });
        rev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i.setWaiting(false);
                i.setReceived(true);
                i.setShipping(false);
            }
        });

    }



}
