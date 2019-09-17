package com.humminbird.machinepartverifierandroid.DataAndPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

public class SettingsManager {
    private static SharedPreferences getSystemPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static boolean signInWithCredentials(String username, String password){
        return false;
    }

    public static boolean aimation(Context context){
        return getSystemPreferences(context).getBoolean("animation",true);
    }

    public static void administratorSignin(Context context, String user, String pass){
        if(signInWithCredentials(user, pass)){
            Toast.makeText(context, "Logged in.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to login.", Toast.LENGTH_SHORT).show();
        }
    }
}
