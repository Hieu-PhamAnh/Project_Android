package com.example.login.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.R;
import com.example.login.api.ApiService;
import com.example.login.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private EditText name,username, password,email;
    private TextView createAccount;
    boolean b = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        name= findViewById(R.id.regis_name);
        username = findViewById(R.id.regis_username);
        password = findViewById(R.id.regis_password);
        email=findViewById(R.id.regis_mail);
        createAccount = findViewById(R.id.register);
        this.createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User u= new User();
                u.setName(name.getText().toString());
                u.setUsername(username.getText().toString());
                u.setPassword(password.getText().toString());
                u.setEmail(email.getText().toString());
                FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
                DatabaseReference reference = rootNode.getReference("user");
                String t= reference.child("user").push().getKey();
                u.setId(t);
                u.setPermission("customer");
                ApiService.apiService.signUp(u.getId(),u).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        startActivity(new Intent(SignUpActivity.this, LoginOptionActivity.class));
                        SignUpActivity.this.finish();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(SignUpActivity.this, "Lỗi " , Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

}
