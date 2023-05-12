package com.example.login.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login.R;
import com.example.login.activity.staff.AdminFunctionActivity;
import com.example.login.api.ApiService;
import com.example.login.model.User;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {


    private EditText username, password;
    private TextView login, createAccount;

    User us ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences sharedPreferences1=LoginActivity.this.getSharedPreferences("app",MODE_PRIVATE);
        SharedPreferences.Editor editor1= sharedPreferences1.edit();
        username = findViewById(R.id.nor_user);
        password = findViewById(R.id.nor_pass);
        login = findViewById(R.id.nor_login);
        createAccount = findViewById(R.id.nor_reg);
        this.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<JsonObject> getUserId= ApiService.apiService.getUserList();
                getUserId.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        //List<String> namesList = new ArrayList<>();
                        try {
                            JSONObject object = new JSONObject(response.body().toString());
                            Iterator<String> stringIterator = object.keys();
                            while (stringIterator.hasNext()) {
                                //namesList.add(stringIterator.next());
                                ApiService.apiService.getUser(stringIterator.next()).enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        if(response.code()==200){
                                            User u=response.body();
                                            if(u.getUsername()!=null&&u.getPassword()!=null){
                                                if(u.getUsername().equals(username.getText().toString())
                                                        &&u.getPassword().equals(password.getText().toString())) {

                                                    if(u.getPermission().equalsIgnoreCase("admin")){
                                                        editor1.putString("user",u.getId());
                                                        editor1.putString("address","");
                                                        editor1.putBoolean("admin",true);
                                                        editor1.commit();
                                                        startActivity(new Intent(LoginActivity.this, AdminFunctionActivity.class));
                                                        LoginActivity.this.finish();
                                                    }else{
                                                        editor1.putString("user",u.getId());
                                                        editor1.putString("address","");
                                                        editor1.putBoolean("admin",false);
                                                        editor1.commit();
                                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                        LoginActivity.this.finish();
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng " , Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng " , Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                LoginActivity.this.finish();
            }
        });
    }
}