package com.alexz.messenger.app;

import com.alexz.messenger.app.util.FirebaseUtil;

import java.util.Timer;
import java.util.TimerTask;

public class ChatApplication extends android.app.Application {

    private boolean running;
    private boolean online;
    private final int REFRESH_RATE = 500;


    @Override
    public void onCreate() {
        super.onCreate();
        running = true;
        online = true;
        FirebaseUtil.setOnline(true);
        Timer mTimer = new Timer();

        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!running && online){
                    FirebaseUtil.setOnline(false);
                    online = false;
                }
            }
        },0, REFRESH_RATE);
    }

    public void setRunning(boolean running) {
        this.running = running;
        if (running && !online){
            FirebaseUtil.setOnline(true);
            online = true;
        }
    }

    @Override
    public void onTerminate() {
        FirebaseUtil.setOnline(false);
        super.onTerminate();
    }
}
