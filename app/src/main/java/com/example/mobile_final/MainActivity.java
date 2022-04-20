package com.example.mobile_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LogoutListener {

    private static final int SPLASH = 3300;

    Animation topAnim, bottomAnim;
    ImageView imageView;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get the login and logout ready
        ((MyApp)getApplication()).registerSessionListener(this);
        ((MyApp)getApplication()).startUserSession();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.text);

        imageView.setAnimation(topAnim);
        textView.setAnimation(bottomAnim);


        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH);
    }

    @Override
    public void onUserInteraction(){
        super.onUserInteraction();
        ((MyApp)getApplication()).onUserInteraction();
    }

    //only when log out, prompt user to log back in
    @Override
    public void onSessionLogout() {
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }
}