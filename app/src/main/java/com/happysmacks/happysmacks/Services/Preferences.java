package com.happysmacks.happysmacks.Services;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
  public static void savePreferences(Context myContext){
    SharedPreferences.Editor editor = myContext.getSharedPreferences("preferences", myContext.MODE_PRIVATE).edit();
    editor.putBoolean("isInitialized", true);
    editor.apply();
  }

  public static boolean retrievePreferences( Context myContext){
    SharedPreferences prefs = myContext.getSharedPreferences("preferences", myContext.MODE_PRIVATE);
    boolean isInitialized = prefs.getBoolean("isInitialized", false);//"No name defined" is the default value.

    return isInitialized;
  }
}
