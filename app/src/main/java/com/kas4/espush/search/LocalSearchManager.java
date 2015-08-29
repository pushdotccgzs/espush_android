package com.kas4.espush.search;

import android.content.Context;
import android.content.Intent;

import com.kas4.espush.app.Constant;
import com.kas4.espush.search.udp.MSG_CFG;

/**
 * Created by kas4 QQ: 1504368178 on 15/8/28.
 */
public class LocalSearchManager {


    private static LocalSearchManager instance;


    Context context;


    private LocalSearchManager(Context context) {
        this.context = context;


    }

    public static LocalSearchManager getInstance(Context context) {
        if (instance == null) {
            instance = new LocalSearchManager(context);
        }

        return instance;
    }


    public static void sendBroadcast(Context context, int msgType) {
        Intent i = new Intent();
        i.setAction(Constant.ACTION_MSG_SEARCH);
        i.putExtra("type", msgType);

        context.sendBroadcast(i);
    }

    private MSG_CFG msg = new MSG_CFG();

    public MSG_CFG getMsg() {
        return msg;
    }

    public void setMsg(MSG_CFG msg) {
        this.msg = msg;
    }
}
