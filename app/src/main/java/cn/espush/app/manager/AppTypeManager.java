package cn.espush.app.manager;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import cn.espush.app.entity.AppTypeEntity;
import cn.espush.app.entity.AppTypeListEntity;
import cn.espush.app.util.SharedPreferencesUtil;

import java.util.List;

/**
 * Created by kas4 QQ: 1504368178 on 15/8/26.
 */
public class AppTypeManager {


    private static final String KEY_APP_TYPE_LOCAL_CACHE = "KEY_APP_TYPE_LOCAL_CACHE";

    public static List<AppTypeEntity> getAppTypeList(Context context) {
        AppTypeListEntity e = getAppTypeListEntity(context);
        if (e != null) {
            return e.getList();
        }
        return null;
    }


    public static synchronized boolean isExist(Context context, AppTypeEntity e) {

        AppTypeListEntity listEnt = getAppTypeListEntity(context);

        List<AppTypeEntity> list = listEnt.getList();

        if (list != null) {
            for (AppTypeEntity o : list) {
                if (o.getAppid().equals(e.getAppid()))
                    return true;
            }
        }
        return false;
    }


    public static synchronized void addAppType(Context context, AppTypeEntity e) {

        AppTypeListEntity listEnt = getAppTypeListEntity(context);

        List<AppTypeEntity> list = listEnt.getList();

        if (list != null)
            list.add(e);

        setAppTypeList(context, listEnt);

    }

    public static synchronized void removeAppType(Context context, AppTypeEntity e) {

        AppTypeListEntity listEnt = getAppTypeListEntity(context);

        List<AppTypeEntity> list = listEnt.getList();
        if (list != null && list.contains(e))
            list.remove(e);

        setAppTypeList(context, listEnt);

    }


    private static AppTypeListEntity getAppTypeListEntity(Context context) {
        AppTypeListEntity listEnt = null;

        String str = SharedPreferencesUtil.getInstance(context).getStringValue(KEY_APP_TYPE_LOCAL_CACHE, "");
        if (TextUtils.isEmpty(str)) {
            listEnt = new AppTypeListEntity();

        } else {
            Gson g = new Gson();
            listEnt = g.fromJson(str, AppTypeListEntity.class);
        }
        return listEnt;
    }

    private static void setAppTypeList(Context context, AppTypeListEntity e) {
        if (e == null) return;

        Gson g = new Gson();
        String str = g.toJson(e);

        SharedPreferencesUtil.getInstance(context).setStringValue(KEY_APP_TYPE_LOCAL_CACHE, str);

    }


}
