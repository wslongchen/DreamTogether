package com.example.mrpan.dreamtogether.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.mrpan.dreamtogether.R;


/**
 * 配置项操作类
 * 
 */

public class MySharePreference {
	private SharedPreferences mPreference;
	private Editor mEditor;

	public MySharePreference(Context context) {
		if (mPreference == null || mEditor == null) {
			mPreference = context.getSharedPreferences(
					context.getString(R.string.app_name), Context.MODE_PRIVATE);
			mEditor = mPreference.edit();
		}
	}

	public void commitInt(String key, int value) {
		if (null == mEditor)
			return;
		mEditor.putInt(key, value);
		mEditor.commit();
	}

	public int getInt(String key, int defValue) {
		if (mPreference == null)
			return defValue;
		return mPreference.getInt(key, defValue);
	}

	public void commitString(String key, String value) {
		if (null == mEditor)
			return;
		mEditor.putString(key, value);
		mEditor.commit();
	}

	public String getString(String key, String defValue) {
		if (mPreference == null)
			return defValue;
		return mPreference.getString(key, defValue);
	}

	public void commitBoolean(String key,boolean value){
		if(null==mEditor)
			return;
		mEditor.putBoolean(key,value);
		mEditor.commit();
	}
	public boolean getBoolean(String key,boolean defValue){
		if(mPreference==null){
			return defValue;
		}
		return mPreference.getBoolean(key,defValue);
	}
}
