package com.kas4.espush.app;

/**
 * Created by kas4 QQ: 1504368178 on 15/8/26.
 */
public class Constant {

//    enum VERTYPE {
//        VER_UNKNOWN=0,
//        VER_AT=1,
//        VER_NODEMCU=2,
//        VER_SDK=3,
//        VER_OTHER=4,
//    };

    enum VERTYPE {
        VER_UNKNOWN, VER_AT, VER_NODEMCU, VER_SDK, VER_OTHER
    }


    public static final int REQUEST_QRCODE = 1001;

    public static final String SPLIT_QRCODE_STRING = "-";

    public static final String KEY_ENTITY = "key_entity";

    public static final String KEY_APPTYPE_ENTITY = "key_apptype_entity";

    public static final int ON_IO_EDGE = 1;
    public static final int OFF_IO_EDGE = 0;

    public static final String ONLINE_DEVICE = "online";
    public static final String OFFLINE_DEVICE = "offline";

    public static final String TYPE_MSG_FORMAT_AT = "AT";
    public static final String TYPE_MSG_FORMAT_LUA = "LUA";

    public static final String ACTION_MSG_SEARCH = "com.kas4.espush.ACTION_MSG_SEARCH";

    public static final int TYPE_MSG_SEARCH_FAIL = -1;
    public static final int TYPE_MSG_SEARCH_CONNECTING = 0;
    public static final int TYPE_MSG_SEARCH_BEEP = 1;
    public static final int TYPE_MSG_SEARCH_SEND_DATA = 2;
    public static final int TYPE_MSG_SEARCH_SEND_OK = 3;


}
