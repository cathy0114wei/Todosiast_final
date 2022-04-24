package com.example.mobile_final;

import android.app.Application;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class MyApp extends Application {
    /*
    private LogoutListener listener;
    private Timer timer;

    public void startUserSession(){
        cancelTimer();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                listener.onSessionLogout();
            }
        }, 5000);
    }

    private void cancelTimer() {
        if (timer != null) timer.cancel();
    }

    public void registerSessionListener(LogoutListener listener){
        this.listener = listener;
    }

    //detect user interaction and retain the log-in status
    public void onUserInteraction() {
        startUserSession();
    }
     */

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null) {
            Intent intent = new Intent(MyApp.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
