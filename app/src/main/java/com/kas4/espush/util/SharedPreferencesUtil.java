package com.kas4.espush.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SharedPreferencesUtil {
	static SharedPreferencesUtil spUtil;
	static SharedPreferences sp;
	
	public synchronized static SharedPreferencesUtil getInstance(Context context) {
		if (spUtil == null) {
			spUtil = new SharedPreferencesUtil();
		}
		sp = PreferenceManager.getDefaultSharedPreferences(context);
		return spUtil;
	}
	
	public int getIntValue(String key, int defValue) {
		return sp.getInt(key, defValue);
	}
	
	public void setIntValue(String key, int value) {
		Editor et = sp.edit();
		et.putInt(key, value);
		et.commit();
	}
	
	public String getStringValue(String key, String defValue) {
		return sp.getString(key, defValue);
	}
	
	public void setStringValue(String key, String value) {
		Editor et = sp.edit();
		et.putString(key, value);
		et.commit();
	}
	
	
}
