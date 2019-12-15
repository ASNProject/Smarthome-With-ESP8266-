package com.asnproject.scanner;

import android.content.Context;
import android.content.SharedPreferences;



public class Preferences {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int private_mode = 0;
    private static final String PREF_NAME= "Tutorial";





    public Preferences (Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, private_mode);
        editor = pref.edit();

    }


    public void setUser (String user){
        editor.putString("user", user);
        editor.commit();

    }

    public String getUser () {
        return pref.getString("user", null);
    }
    public void setPass (String pass){
        editor.putString("password", pass);
        editor.commit();



    }

    public String getPass () {
        return pref.getString("password", null);
    }

    public void setPort (String port){
        editor.putString("port", port);
        editor.commit();

}

    public String getPort () {
        return pref.getString("port", null);
    }

}
