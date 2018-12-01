package com.common;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.app.MyApp;

public class SystemSetting {
    public static final String CONTROL_MODE = "CONTROL_MODE";
    public static SharedPreferences mSettings;

    static {
        mSettings = PreferenceManager.getDefaultSharedPreferences(MyApp.getInstances());
    }

    public static void setProperty(String key, String value) {
        SharedPreferences.Editor editer = mSettings.edit();
        editer.putString(key, value);
        editer.commit();
    }

    public static String getProperty(String key, String defaultValue) {
        return SystemSetting.mSettings.getString(key, defaultValue);
    }

    public static void clearProperty(String key) {
        SharedPreferences.Editor editer = mSettings.edit();
        editer.remove(key);
        editer.commit();
    }

    public static boolean containProperty(String key) {
        return mSettings.contains(key);
    }
}
