package com.example.michal.komunikator.messenger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.michal.komunikator.Constants;
import com.example.michal.komunikator.Gallery.gesturesLists.GalleryActivity;
import com.example.michal.komunikator.Gesture;
import com.example.michal.komunikator.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static android.provider.ContactsContract.CommonDataKinds.StructuredName.PREFIX;

public class MessengerActivity extends FragmentActivity{

    private boolean mListen = true;

    private ArrayList<Gesture> mNewMessageList = new ArrayList<>();
    private RecyclerView mNewMessageView;
    private MessageAdapter mNewMessageAdapter;

    private ArrayList<ArrayList<Gesture>> mChatList = new ArrayList<>();;

    private  String mResponseFromServer = "";

    private SharedPreferences mSettings = null;
    private String mSender = null;
    private String mReceiver = null;
    private String mIp = null;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        mNewMessageView = (RecyclerView)findViewById(R.id.newMessage);
        mNewMessageAdapter = new MessageAdapter(this, mNewMessageList);

        RecyclerView.LayoutManager newMessageLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mNewMessageView.setLayoutManager(newMessageLayoutManager);

        mNewMessageView.setAdapter(mNewMessageAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mListen = true;
        checkForMessages();

        mSettings = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0);
        mIp = mSettings.getString(Constants.SERVER_IP_ID, Constants.DEFAULT_SERVER_IP);
        mSender = mSettings.getString(Constants.SENDER_NAME_ID, Constants.DEFAULT_SENDER);
        mReceiver = mSettings.getString(Constants.RECEIVER_NAME_ID, Constants.DEFAULT_RECEIVER);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mListen = false;
    }

    public void createMessage(View view){
        Intent i = new Intent(this, GalleryActivity.class);
        startActivityForResult(i, Constants.CHOOSEN_GESTURES_LIST_REQUEST);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.CHOOSEN_GESTURES_LIST_REQUEST) {
            ArrayList<String> tmpList;
            tmpList = data.getStringArrayListExtra("MESSAGE");
            Log.i("MessengerActivity", "Created message's length is " + Integer.toString(tmpList.size()));

            mNewMessageList = Gesture.findDrawableByName(tmpList);
            mNewMessageAdapter = new MessageAdapter(this, mNewMessageList);
            mNewMessageView.setAdapter(mNewMessageAdapter);
        }
    }

    private void addMessageToConversation(ArrayList<Gesture> message){
        mChatList.add(message);
        ListView chatListView = (ListView)findViewById(R.id.chatListView);
        ChatListViewAdapter adapter = new ChatListViewAdapter(this, R.layout.chat_message_layout, mChatList);
        chatListView.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void sendMessage(View view) throws UnsupportedEncodingException {
       addMessageToConversation(mNewMessageList);

        Message msg = new Message(mSender, mNewMessageList);
        Gson gson = new GsonBuilder().create();
        String jsonString = gson.toJson(msg);
        jsonString = URLEncoder.encode(jsonString, "utf-8");

        mNewMessageList = new ArrayList<>();
        this.mNewMessageAdapter = new MessageAdapter(this, mNewMessageList);
        mNewMessageView.setAdapter(this.mNewMessageAdapter);

        httpRequest("message=" + jsonString, mReceiver);
    }

    public void checkForMessages(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mListen) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String request;
                            request = httpRequest("check=true", mSender);
                            if (request.contains("No new messages!")) {
                                Log.i("checkForMessages", request);
                            } else if (request.contains("messageLength")) {
                                Log.i("checkForMessages", request);
                                Gson gson = new Gson();
                                Message msg = gson.fromJson(request, Message.class);
                                addMessageToConversation(Gesture.findDrawableByName(msg.getMessage()));
                            }
                        }
                    });
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    }
                }
        }).start();
    }

    private String httpRequest(String message, String receiver){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + mIp;
        url += "/?name="+ receiver + "&" + message + "\n";

        Log.i("message", url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mResponseFromServer = response;
                        Log.i("responseFromServer", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("httpRequest", "Cannot connect to server");
                mResponseFromServer = "";
            }
        });
        queue.add(stringRequest);
        return mResponseFromServer;
    }

}
