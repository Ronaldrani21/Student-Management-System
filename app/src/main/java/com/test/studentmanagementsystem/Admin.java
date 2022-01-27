package com.test.studentmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Admin extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_assistant);

        Button vUser = findViewById(R.id.button2);
        Button vStdRes = findViewById(R.id.button5);
        Button logout = findViewById(R.id.button6);

        vStdRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adminRes = new Intent(Admin.this, ViewResults.class);
                startActivity(adminRes);
            }
        });

        vUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adminVUser = new Intent(Admin.this, ViewUsers.class);
                startActivity(adminVUser);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent adminVUser = new Intent(Admin.this, MainActivity.class);
                startActivity(adminVUser);
            }
        });
    }
}


