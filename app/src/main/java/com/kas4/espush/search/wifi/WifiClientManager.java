package com.kas4.espush.search.wifi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import com.kas4.espush.search.udp.UdpService;

public class WifiClientManager {


    private static WifiClientManager sInstance = null;
    private Context mContext;
    private android.net.wifi.WifiManager mWifiManager;

    public static WifiClientManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new WifiClientManager(context);
        }
        return sInstance;
    }

    private WifiClientManager(Context context) {
        mContext = context;

        mWifiManager = (android.net.wifi.WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);

    }


    public boolean search(String ssid, String pwd) {

        boolean res = false;

        if (mWifiManager.isWifiEnabled() == false) {
            toWifiSettings((Activity) mContext);
            return false;
        }

        Intent i = new Intent(mContext, UdpService.class);
        mContext.startService(i);

        return true;
    }


    public static void toWifiSettings(Activity activity) {
        Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
        activity.startActivity(i);
    }


    //   #### test

}