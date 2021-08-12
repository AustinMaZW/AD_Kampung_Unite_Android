package com.example.ad_project_kampung_unite;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartUpActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        Button loginBtn = findViewById(R.id.loginButton);
        Button registerBtn = findViewById(R.id.registerButton);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginBtn != null){
                    Intent login = new Intent(StartUpActivity.this, LoginActivity.class);
                    startActivity(login);
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (registerBtn != null){
                    Intent register = new Intent(StartUpActivity.this, RegisterActivity.class);
                    startActivity(register);
                }
            }
        });
    }

    //default onBackPressed() will quit app
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StartUpActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();


    }
}