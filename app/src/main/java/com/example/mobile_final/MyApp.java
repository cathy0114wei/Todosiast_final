package com.example.mobile_final;

import android.app.Application;
import java.util.Timer;
import java.util.TimerTask;

public class MyApp extends Application {
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
        }, 10000);
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
}