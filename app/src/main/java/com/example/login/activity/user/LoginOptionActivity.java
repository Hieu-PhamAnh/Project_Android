package com.example.login.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login.R;
import com.example.login.activity.staff.AdminFunctionActivity;
import com.example.login.api.ApiService;
import com.example.login.model.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginOptionActivity extends AppCompatActivity {
    private TextView lgInsta, lg, lgMail;
    private LoginButton lgFb;
    private static final String EMAIL = "email";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.fullyInitialize();
        SharedPreferences sharedPreferences1=LoginOptionActivity.this.getSharedPreferences("app",MODE_PRIVATE);
        SharedPreferences.Editor editor1= sharedPreferences1.edit();
        if(!sharedPreferences1.getString("user","").equals("")){
            if(sharedPreferences1.getBoolean("admin",true))
                startActivity(new Intent(LoginOptionActivity.this, AdminFunctionActivity.class));
            else startActivity(new Intent(LoginOptionActivity.this, MainActivity.class));
            LoginOptionActivity.this.finish();
        }
        setContentView(com.example.login.R.layout.activity_login_option);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        lg = findViewById(R.id.lg_nor);
        lgInsta = findViewById(R.id.lg_insta);
        lgFb = findViewById(R.id.lg_fb);
        CallbackManager callbackManager = CallbackManager.Factory.create();
        lgFb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {
                        try {
                            String id = jsonObject.getString("id");
                            String name = jsonObject.getString("name");
                            User u= new User();
                            u.setName(name);
                            u.setId(id);
                            u.setPermission("customer");
                            ApiService.apiService.signUp(u.getId(),u).enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    editor1.putString("user",id);
                                    editor1.putString("address","");
                                    editor1.putBoolean("admin",false);
                                    editor1.commit();
                                    startActivity(new Intent(LoginOptionActivity.this, MainActivity.class));
                                    LoginOptionActivity.this.finish();
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {

                                }
                            });

                        } catch (JSONException e) {
                            System.out.println(e);
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Toast.makeText(LoginOptionActivity.this, e+"" , Toast.LENGTH_SHORT).show();
            }
        });
        lgMail = findViewById(R.id.lg_mail);
        lg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginOptionActivity.this, LoginActivity.class));
                LoginOptionActivity.this.finish();
            }
        });
    }
}