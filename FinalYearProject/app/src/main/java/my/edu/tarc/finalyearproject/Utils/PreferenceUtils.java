package my.edu.tarc.finalyearproject.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

import my.edu.tarc.finalyearproject.HomeFragment;


/**
 * Created by delaroy on 3/26/18.
 */

public class PreferenceUtils {

    public PreferenceUtils(){

    }

    public static boolean saveEmail(String email, OnCompleteListener<AuthResult> context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences((Context) context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_EMAIL, email);
        prefsEditor.apply();
        return true;
    }
/*
    public static String getEmail(HomeFragment context) {
        // prefs = PreferenceManager.getDefaultSharedPreferences(Context);
        return prefs.getString(Constants.KEY_EMAIL, null);
    }*/

    public static boolean savePassword(String password, OnCompleteListener<AuthResult> context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences((Context) context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_PASSWORD, password);
        prefsEditor.apply();
        return true;
    }

    public static String getPassword(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_PASSWORD, null);
    }
}
