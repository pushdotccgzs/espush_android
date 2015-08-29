package com.kas4.espush.manager;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.kas4.espush.entity.AppsResponse;
import com.kas4.espush.entity.ResultResponse;
import com.kas4.espush.util.MD5Util;
import com.kas4.espush.volley.HttpTool;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by kas4 QQ: 1504368178 on 15/8/26.
 */
public class NetAPI {

    static final String URL_HOST = "https://espush.cn/openapi/";

    static final String URL_GET_APPS = URL_HOST + "apps/";

    static final String URL_GET_DEVICE_LIST = URL_HOST + "openapi/devices/list/";

    static final String URL_GET_IO_STATUS = URL_HOST + "gpio_status/";

    static final String URL_REFRESH_DEVICE_ALIVE = URL_HOST + "manual_refresh/";

    static final String URL_PUSH_MESSAGE = URL_HOST + "dev/push/message/";

    static final String URL_SET_IO_EDGE = URL_HOST + "set_gpio_edge/";

    public static void getApps(String appid, String appkey, Response.Listener success, Response.ErrorListener error) {

        HashMap<String, String> m =
                buildParamMap(appid, appkey);

        String url = getUrl(URL_GET_APPS, Request.Method.GET, m, appid);
        HttpTool.get(AppsResponse.class, url, success, error);


    }

    /**
     * @param appid
     * @param appkey
     * @param success
     * @param error
     * @deprecated
     */
    public static void getDeviceList(String appid, String appkey, Response.Listener success, Response.ErrorListener error) {


        HashMap<String, String> m =
                buildParamMap(appid, appkey);

        String url = getUrl(URL_GET_DEVICE_LIST, Request.Method.GET, m, appid);
        HttpTool.get(AppsResponse.class, url, success, error);


    }


    public static void getIOStatus(String chipid, String appid, String appkey, Response.Listener success, Response.ErrorListener error) {

        StringBuilder sb = new StringBuilder(URL_GET_IO_STATUS);
        sb.append(chipid);
        sb.append("/");

        HashMap<String, String> m =
                buildParamMap(appid, appkey);


        String url = getUrl(sb.toString(), Request.Method.GET, m, appid);

        HttpTool.get(ResultResponse.class, url, success, error);

    }

    public static void setGpioEdge(String chipid, String pin, String edge, String appid, String appkey, Response.Listener success, Response.ErrorListener error) {

        StringBuilder sb = new StringBuilder(URL_SET_IO_EDGE);
        sb.append(chipid);
        sb.append("/");
        sb.append(pin);
        sb.append("/");
        sb.append(edge);
        sb.append("/");

        HashMap<String, String> m =
                buildParamMap(appid, appkey);


        String url = getUrl(sb.toString(), Request.Method.GET, m, appid);

        HttpTool.get(ResultResponse.class, url, success, error);

    }

    public static void refreshDevice(String chipid, String appid, String appkey, Response.Listener success, Response.ErrorListener error) {

        StringBuilder sb = new StringBuilder(URL_REFRESH_DEVICE_ALIVE);
        sb.append(chipid);
        sb.append("/");

        HashMap<String, String> m =
                buildParamMap(appid, appkey);


        String url = getUrl(sb.toString(), Request.Method.GET, m, appid);

        HttpTool.get(ResultResponse.class, url, success, error);

    }


    public static void pushMessage(String chipid, String msgformat, String message, String appid, String appkey, Response.Listener success, Response.ErrorListener error) {

        StringBuilder sb = new StringBuilder(URL_PUSH_MESSAGE);


        HashMap<String, String> m =
                buildParamMap(appid, appkey);
        m.put("devid", chipid);
        m.put("msgformat", msgformat);
        m.put("message", message);

        String url = getUrl(sb.toString(), Request.Method.POST, m, appid);

        HttpTool.post(ResultResponse.class, url, success, error);

    }


    // ###

    private static HashMap<String, String> buildParamMap(String appid, String appkey) {
        HashMap<String, String> m = new HashMap<>();
        m.put("appid", appid + appkey);// 用于加密
        m.put("timestamp", "" + System.currentTimeMillis() / 1000);
        return m;
    }

    // appid用于还原
    private static String getUrl(String url, int method, HashMap<String, String> p, String appid) {


        TreeMap<String, String> map = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o, String t1) {
                return t1.compareTo(o);
            }
        });

        for (String k : p.keySet()) {
            String v = p.get(k).toLowerCase();
            map.put(k, v);
        }


        String sign = getSign(method, map);

        p.put("sign", sign);
        p.put("appid", appid);

        Uri.Builder b = Uri.parse(url).buildUpon();
        for (String k : p.keySet()) {
            String v = p.get(k);
            b.appendQueryParameter(k, v);
        }
        return b.toString();
    }

    private static String getSign(int method, Map<String, String> p) {
        StringBuilder sb = new StringBuilder();
        if (method == Request.Method.GET) {
            sb.append("get");
        } else if (method == Request.Method.POST) {
            sb.append("post");
        }

        boolean isStart = true;

        for (String k : p.keySet()) {
            String v = p.get(k);
            if (isStart) {
                isStart = false;
            } else {
                sb.append("&");
            }
            sb.append(k);
            sb.append("=");
            sb.append(v);
        }
        return MD5Util.getMd5String(sb.toString());

    }

}
