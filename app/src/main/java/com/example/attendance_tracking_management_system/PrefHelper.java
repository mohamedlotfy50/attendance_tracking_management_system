package com.example.attendance_tracking_management_system;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class PrefHelper {
    private SharedPreferences shared;
    private SharedPreferences.Editor pref;
    final  Context context;

    PrefHelper(Context context,String name){
        this.context =context;
        shared =this.context.getSharedPreferences(name,context.MODE_PRIVATE);
        pref = shared.edit();
    }


    public void setString(String key,String val){
        pref.putString(key,val);
    }
    public void setMap(String key, Map val){
        pref.putString(key,val.toString());
        pref.commit();


    }
    public boolean keyExists(String key){
        return shared.contains(key);
    }
    public String getString(String key){
        return  shared.getString(key,null);
    }
    public Map getMap(String key){
        String value = shared.getString(key,null) ;
        value = value.substring(1, value.length()-1);           //remove curly brackets
        String[] keyValuePairs = value.split(",");              //split the string to creat key-value pairs
        Map<String,String> map = new HashMap<>();

        for(String pair : keyValuePairs)                        //iterate over the pairs
        {
            String[] entry = pair.split("=");                   //split the pairs to get key and value
            map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
        }

        return  map;
    }
    public void deleteKey(String key){
        pref.remove(key);
        pref.apply();

    }

}
