package com.kas4.espush.util;




import android.content.Context;
import android.widget.Toast;


public class ToastUtil {
	

	
	
	public static void showToast(Context context, CharSequence text,int duration) {
		
		Toast.makeText(context, text, duration).show();	
		
	}
	
	public static void showShortToast(Context context, CharSequence text) {
		
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();	
		
	}



	

	

	
	
}
