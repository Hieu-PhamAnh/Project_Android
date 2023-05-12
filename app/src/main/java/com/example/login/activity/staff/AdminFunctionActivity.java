package com.example.login.activity.staff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.login.R;
import com.example.login.activity.user.LoginOptionActivity;
import com.example.login.activity.user.MainActivity;
import com.example.login.activity.user.WatchOderActivity;

public class AdminFunctionActivity extends AppCompatActivity {
    private TextView muser,sorder,addi,imi,iorder,statistic,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.function_activity);
        SharedPreferences sharedPreferences1= AdminFunctionActivity.this.getSharedPreferences("app",MODE_PRIVATE);
        SharedPreferences.Editor editor1= sharedPreferences1.edit();
        muser= findViewById(R.id.auser);
        sorder=findViewById(R.id.asorder);
        sorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(AdminFunctionActivity.this, WatchOderActivity.class);
                intent.putExtra("type", "buy");
                startActivity(intent);

            }
        });
        addi=findViewById(R.id.aadditem);
        imi=findViewById(R.id.aimport);
        imi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminFunctionActivity.this, MainActivity.class));
            }
        });
        iorder=findViewById(R.id.aiorder);
        iorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(AdminFunctionActivity.this, WatchOderActivity.class);
                intent.putExtra("type", "import");
                startActivity(intent);

            }
        });
        statistic=findViewById(R.id.astatistic);
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor1.putString("user","");
                editor1.commit();
                startActivity(new Intent(AdminFunctionActivity.this, LoginOptionActivity.class));
            }
        });
        addi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminFunctionActivity.this, AddItemActivity.class));
            }
        });
    }
}