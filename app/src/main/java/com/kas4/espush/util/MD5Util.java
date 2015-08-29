package com.kas4.espush.util;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 用于生成md5字符
 */
public class MD5Util {
	private static final String TAG = "MD5Utils";
	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	protected static MessageDigest messageDigest = null;
	static {
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			Log.e(TAG, "FileMd5Utils NoSuchAlgorithmException " + e.toString());
		}
	}

	/**
	 * MD5 加密
	 */

	public static String getMd5String(String oringalString) {
		String returnString = null;
		try {
			byte[] result = MessageDigest.getInstance("MD5").digest(
					(oringalString).getBytes());
			StringBuffer md5StrBuff = new StringBuffer();

			for (int i = 0; i < result.length; i++) {
				if (Integer.toHexString(0xFF & result[i]).length() == 1) {
					md5StrBuff.append("0").append(
							Integer.toHexString(0xFF & result[i]));
				} else {
					md5StrBuff.append(Integer.toHexString(0xFF & result[i]));
				}
			}
			returnString = md5StrBuff.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			return returnString;
		}
	}

}
