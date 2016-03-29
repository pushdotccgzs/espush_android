package cn.espush.app.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;

public class HttpTool {

    private static RequestQueue queue = null;

    public static void init(Context context) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
    }

    public static <T> void get(Class<T> cls, String url, Listener<T> success, ErrorListener error) {
        request(cls, Request.Method.GET, url, success, error);
    }
    public static <T> void post(Class<T> cls, String url, Listener<T> success, ErrorListener error) {
        request(cls, Request.Method.POST, url, success, error);
    }


    private static <T> void request(Class<T> cls, int method, String url, Listener<T> listener, ErrorListener errorListener) {
        if (queue == null) {
            return;
        }

        GsonRequest<T> req = new GsonRequest<T>(method, url,
                cls, null, listener, errorListener);
        queue.add((Request<?>) req);

    }


}
