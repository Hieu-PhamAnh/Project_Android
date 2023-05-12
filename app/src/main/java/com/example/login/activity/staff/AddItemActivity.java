package com.example.login.activity.staff;

import android.content.ContentResolver;
import android.content.Intent;
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
import com.example.login.api.ApiService;
import com.example.login.model.Item;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddItemActivity extends AppCompatActivity {
    private EditText iname, ibrand, price,price2;
    private ImageView image;
    private TextView addi,home;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("item");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.additem_activity);
        iname= findViewById(R.id.iname);
        ibrand=findViewById(R.id.ibrand);
        addi=findViewById(R.id.additem);
        price=findViewById(R.id.iprice);
        price2=findViewById(R.id.iprice2);
        image=findViewById(R.id.iimg);
        home=findViewById(R.id.bhome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddItemActivity.this, AdminFunctionActivity.class));
                AddItemActivity.this.finish();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent , 2);
            }
        });
        addi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null){
                    uploadToFirebase(imageUri);
                }else{
                    Toast.makeText(AddItemActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==2 && resultCode == RESULT_OK && data != null){

            imageUri = data.getData();
            image.setImageURI(imageUri);

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
                        String modelId = root.push().getKey();
                        Item i= new Item(modelId,iname.getText().toString(),
                                Long.parseLong(price.getText().toString()),Long.parseLong(price.getText().toString()),0,ibrand.getText().toString(),
                                uri.toString());
                        ApiService.apiService.editItem(modelId,i).enqueue(new Callback<Item>() {
                            @Override
                            public void onResponse(Call<Item> call, Response<Item> response) {
                                Toast.makeText(AddItemActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddItemActivity.this, AdminFunctionActivity.class));
                                AddItemActivity.this.finish();
                            }

                            @Override
                            public void onFailure(Call<Item> call, Throwable t) {
                                Toast.makeText(AddItemActivity.this, "Upload không thành cóng", Toast.LENGTH_SHORT).show();
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

