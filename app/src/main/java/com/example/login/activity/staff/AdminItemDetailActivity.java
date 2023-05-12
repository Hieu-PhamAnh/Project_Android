package com.example.login.activity.staff;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login.R;
import com.example.login.activity.user.LoginOptionActivity;
import com.example.login.activity.user.MainActivity;
import com.example.login.activity.user.OrderActivity;
import com.example.login.api.ApiService;
import com.example.login.model.Item;
import com.example.login.model.OrderItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminItemDetailActivity extends AppCompatActivity {
    private TextView  instore,edit,delete,addoi,home,cart,logout;
    private EditText name, brand,price,iprice;
    private ImageView img;
    private Item i=new Item();
    private OrderItem o;
    int c=0;
    private Uri imageUri;

    FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String id = intent.getStringExtra("item");
        setContentView(R.layout.adminitemdetail_activity);
        SharedPreferences sharedPreferences1= AdminItemDetailActivity.this.getSharedPreferences("app",MODE_PRIVATE);
        String user = sharedPreferences1.getString("user", "");
        SharedPreferences.Editor editor1= sharedPreferences1.edit();
        name=findViewById(R.id.ainame);
        brand=findViewById(R.id.aibrand);
        price=findViewById(R.id.aiprice);
        iprice=findViewById(R.id.aiprice2);
        instore=findViewById(R.id.ainstore);
        edit=findViewById(R.id.edititem);
        delete=findViewById(R.id.delitem);
        addoi= findViewById(R.id.addtocart);
        img=findViewById(R.id.aiimg);
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor1.putString("user","");
                editor1.commit();
                startActivity(new Intent(AdminItemDetailActivity.this, LoginOptionActivity.class));
                AdminItemDetailActivity.this.finish();
            }
        });
        ApiService.apiService.getItem(id).enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if(response.code()==200){
                    i=response.body();
                    name.setText(i.getName());
                    brand.setText(i.getBrand());
                    price.setText(i.getPrice()+"");
                    iprice.setText(i.getImprice()+"");
                    instore.setText("Trong Kho :"+ i.getInstore());
                    Picasso.get().load(i.getImg()).into(img);
                }
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
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

                                            String t =  rootNode.getReference("orderitem").push().getKey();
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
                String t = rootNode.getReference("orderitem").push().getKey();
                o.setId(t);
            }
        });

        cart=findViewById(R.id.imcart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminItemDetailActivity.this, OrderActivity.class));
                AdminItemDetailActivity.this.finish();
            }
        });
        home=findViewById(R.id.bhome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminItemDetailActivity.this, MainActivity.class));
                AdminItemDetailActivity.this.finish();
            }
        });
        addoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiService.apiService.editOrderItem(o.getId(),o).enqueue(new Callback<OrderItem>() {
                    @Override
                    public void onResponse(Call<OrderItem> call, Response<OrderItem> response) {
                        startActivity(new Intent(AdminItemDetailActivity.this, MainActivity.class));
                        AdminItemDetailActivity.this.finish();
                    }

                    @Override
                    public void onFailure(Call<OrderItem> call, Throwable t) { }
                });
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent , 2);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null){
                    uploadToFirebase(imageUri);
                }else{
                    i.setName(name.getText().toString());
                    i.setBrand(brand.getText().toString());
                    i.setPrice( Long.parseLong(price.getText().toString()));
                    i.setImprice(Long.parseLong(iprice.getText().toString()));
                    ApiService.apiService.editItem(i.getId(),i).enqueue(new Callback<Item>() {
                        @Override
                        public void onResponse(Call<Item> call, Response<Item> response) {
                            Toast.makeText(AdminItemDetailActivity.this, "Edit Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AdminItemDetailActivity.this, MainActivity.class));
                            AdminItemDetailActivity.this.finish();
                        }

                        @Override
                        public void onFailure(Call<Item> call, Throwable t) {
                            Toast.makeText(AdminItemDetailActivity.this, "Edit không thành cóng", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiService.apiService.deleteItem(i.getId()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(AdminItemDetailActivity.this, "Delete Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminItemDetailActivity.this, MainActivity.class));
                        AdminItemDetailActivity.this.finish();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==2 && resultCode == RESULT_OK && data != null){

            imageUri = data.getData();
            img.setImageURI(imageUri);

        }
    }
    private void uploadToFirebase(Uri uri){

        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        i.setName(name.getText().toString());
                        i.setBrand(brand.getText().toString());
                        i.setPrice( Long.parseLong(price.getText().toString()));
                        i.setImprice(Long.parseLong(iprice.getText().toString()));
                        i.setImg(uri.toString());

                        ApiService.apiService.editItem(i.getId(),i).enqueue(new Callback<Item>() {
                            @Override
                            public void onResponse(Call<Item> call, Response<Item> response) {
                                Toast.makeText(AdminItemDetailActivity.this, "Edit Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AdminItemDetailActivity.this, MainActivity.class));
                                AdminItemDetailActivity.this.finish();
                            }

                            @Override
                            public void onFailure(Call<Item> call, Throwable t) {
                                Toast.makeText(AdminItemDetailActivity.this, "Edit không thành cóng", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                });
            }

        });
    }

    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

}
