package com.test.studentmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HOD_Dashboard extends AppCompatActivity {
    //    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);


        Button mgUser = findViewById(R.id.button1);
        Button mgResults = findViewById(R.id.manageResultBtn);
        Button vUser = findViewById(R.id.button2);
        Button vStdRes = findViewById(R.id.button5);
        Button logout = findViewById(R.id.button6);

        mgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adminReg = new Intent(HOD_Dashboard.this, RegisterUser.class);
                startActivity(adminReg);
            }
        });
        mgResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adminStd = new Intent(HOD_Dashboard.this, Student_result.class);
                startActivity(adminStd);
            }
        });

        vStdRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adminRes = new Intent(HOD_Dashboard.this, ViewResults.class);
                startActivity(adminRes);
            }
        });

        vUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adminVUser = new Intent(HOD_Dashboard.this, ViewUsers.class);
                startActivity(adminVUser);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adminVUser = new Intent(HOD_Dashboard.this, MainActivity.class);
                startActivity(adminVUser);
            }
        });
    }
    }
