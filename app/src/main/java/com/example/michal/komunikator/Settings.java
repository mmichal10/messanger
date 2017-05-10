package com.example.michal.komunikator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import java.net.Inet4Address;
import java.net.InetAddress;

public class Settings extends Activity {
    private SharedPreferences mSettings;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        EditText ipEditText = (EditText)findViewById(R.id.ipEditText);
        EditText receiverEditText = (EditText)findViewById(R.id.receiverEditText);
        EditText senderEditText = (EditText)findViewById(R.id.senderEditText);

        mSettings = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0);
        mEditor = mSettings.edit();

        ipEditText.setHint(mSettings.getString(Constants.SERVER_IP_ID, Constants.DEFAULT_SERVER_IP));
        receiverEditText.setHint(mSettings.getString(Constants.RECEIVER_NAME_ID, Constants.DEFAULT_RECEIVER));
        senderEditText.setHint(mSettings.getString(Constants.SENDER_NAME_ID, Constants.DEFAULT_SENDER));
    }

    public void changeServerIp(View view){
        EditText ipEditText = (EditText)findViewById(R.id.ipEditText);
        String newIP = ipEditText.getText().toString();

        if(!isValidIP(newIP))
            ipEditText.setText("niepoprawny adres!");

        Log.i("new servers IP", newIP);

        mEditor.putString(Constants.SERVER_IP_ID, newIP);
        mEditor.commit();

        ipEditText.setText("");
        ipEditText.setHint(newIP);
    }

    public void changeReceiverName(View view){
        EditText nameEditText = (EditText)findViewById(R.id.receiverEditText);
        String newName = nameEditText.getText().toString();

        newName = newName.replaceAll("\\s+", "");

        if(newName.isEmpty() || newName.length() < 1)
            return;

        Log.i("new receiver name", newName);

        mEditor.putString(Constants.RECEIVER_NAME_ID, newName);
        mEditor.commit();

        nameEditText.setText("");
        nameEditText.setHint(newName);
    }

    public void changeSenderName(View view){
        EditText nameEditText = (EditText)findViewById(R.id.senderEditText);
        String newName = nameEditText.getText().toString();

        newName = newName.replaceAll("\\s+", "");

        if(newName.isEmpty() || newName.length() < 1)
            return;

        Log.i("new sender name", newName);

        mEditor.putString(Constants.SENDER_NAME_ID, newName);
        mEditor.commit();

        nameEditText.setText("");
        nameEditText.setHint(newName);
    }

    private boolean isValidIP(String ip){
        try{
            if(ip == null || ip.isEmpty())
                return false;

            String[] parts = ip.split("\\.");
            if(parts.length != 4)
                return false;

            for(String s : parts){
                int i = Integer.parseInt(s);
                if(i < 0 || i > 255)
                    return false;
            }

            if(ip.endsWith("."))
                return false;
        } catch (NumberFormatException e){
            return false;
        }

        return true;
    }
}
