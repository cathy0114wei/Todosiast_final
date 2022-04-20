package com.example.mobile_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    private EditText loginEmail, loginPwd;

    private FirebaseAuth mAuth;
    private ProgressDialog loader;
//    private String username = "";
//    private String psw = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.loginToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Login");

        mAuth = FirebaseAuth.getInstance();
        loader = new ProgressDialog(this);

        loginEmail = findViewById(R.id.loginEmail);
        //username  = loginEmail.toString();
        loginPwd = findViewById(R.id.loginPassword);
        //psw = loginPwd.toString();
        Button loginBtn = findViewById(R.id.loginButton);
        TextView loginQn = findViewById(R.id.loginPageQuestion);

        loginQn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });
        loginBtn.setOnClickListener(v -> {
            String email = loginEmail.getText().toString().trim();
            String password = loginPwd.getText().toString().trim();
            if (TextUtils.isEmpty(email)){
                loginEmail.setError("Email is required!");
            }
            if (TextUtils.isEmpty(password)){
                loginPwd.setError("Password required!");
            } else {
                loader.setMessage("Login in progress");
                loader.setCanceledOnTouchOutside(false);
                loader.show();

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.putExtra("username", email);
                            intent.putExtra("password", password);
                            startActivity(intent);
                            finish();
                        } else {
                            if (task.getException() != null) {
                                String error = task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Login falied" + error, Toast.LENGTH_SHORT).show();
                            }
                        }
                        loader.dismiss();
                    }
                });
            }

        });
    }

}