package com.example.musicbook.PreferencesManager;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveLoginData {
    private final SharedPreferences sharedPreferences;
    public SaveLoginData(Context context){
        sharedPreferences=context.getSharedPreferences("MY_SHARE_REFERENCE",Context.MODE_PRIVATE);
    }
    public void putAccount(String key,String value){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }
    public String getAccount(String key){
        return sharedPreferences.getString(key, null)  ;
    }
    public void clear(){
        
    }
}
