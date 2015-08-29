package com.kas4.espush.search.udp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

/**
 * Created by kas4 QQ: 1504368178 on 15/8/28.
 */
public class UdpService extends Service {


    private final Handler mHandler = new Handler();

    Context mContext = this;


    protected void handle(Intent intent) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                UdpManager.getInstance(mContext).startUDPListen();
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                UdpManager.getInstance(mContext).sendBeep();
                
            }
        }).start();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handle(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        UdpManager.getInstance(this).shutUDP();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
