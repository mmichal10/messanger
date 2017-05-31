package com.example.michal.komunikator;

import android.content.Context;

/**
 * Created by michal on 17.05.2017.
 */

public class App extends android.app.Application{
    private static App mApp = null;

    @Override
    public void onCreate(){
        super.onCreate();
        mApp = this;
    }

    public static Context context(){
        return mApp.getApplicationContext();
    }
}