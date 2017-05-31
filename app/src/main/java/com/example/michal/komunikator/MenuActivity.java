package com.example.michal.komunikator;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.michal.komunikator.Gallery.gesturesLists.GalleryActivity;
import com.example.michal.komunikator.R;
import com.example.michal.komunikator.messenger.MessengerActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        SharedPreferences settings = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0);
        if(!settings.contains("serverIP")){
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("serverIP", Constants.DEFAULT_SERVER_IP);
            editor.putString("senderName", Constants.DEFAULT_SENDER);
            editor.putString("receiverName", Constants.DEFAULT_RECEIVER);
            editor.commit();
        }
    }

    public void makatonGallery(View view){
        Intent i = new Intent(this, GalleryActivity.class);
        this.startActivity(i);
    }

    public void messenger(View view){
        Intent i = new Intent(this, MessengerActivity.class);
        this.startActivity(i);
    }

    public void settings(View view){
        Intent i = new Intent(this, Settings.class);
        this.startActivity(i);
    }

    public Context getContext(){
        return getApplicationContext();
    }

    public void exit(View view){
        onBackPressed();
    }
}
