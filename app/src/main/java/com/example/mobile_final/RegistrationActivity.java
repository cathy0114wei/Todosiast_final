package com.example.mobile_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

public class RegistrationActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private EditText RegEmail, RegPwd;
    private Button RegBtn;
    private TextView RegnQn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        toolbar = findViewById(R.id.RegistrationToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Registration");

        RegEmail = findViewById(R.id.RegistrationEmail);
        RegPwd = findViewById(R.id.RegistrationPassword);
        RegBtn = findViewById(R.id.RegistrationButton);
        RegnQn = findViewById(R.id.RegistrationPageQuestion);

        RegnQn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}