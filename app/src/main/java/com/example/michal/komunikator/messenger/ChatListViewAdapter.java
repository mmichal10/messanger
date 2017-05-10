package com.example.michal.komunikator.messenger;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.michal.komunikator.Gesture;
import com.example.michal.komunikator.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.michal.komunikator.R.id.newMessage;

/**
 * Created by michal on 20.04.2017.
 */

public class ChatListViewAdapter extends ArrayAdapter<ArrayList<Gesture>> {

    private Context context = null;
    private List<ArrayList<Gesture>> chatList = null;
    private int resource;


    public ChatListViewAdapter(Context context, int resource, List<ArrayList<Gesture>> list) {
        super(context, resource);
        this.context = context;
        this.chatList = list;
        this.resource = resource;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Nullable
    @Override
    public ArrayList<Gesture> getItem(int position) {
       return chatList.get(position);
    }

    @Override
    public int getCount() {
        return chatList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        ArrayList<Gesture> message = getItem(position);

        if(v == null){
            LayoutInflater vi = LayoutInflater.from(getContext());
            v = vi.inflate(resource, null);
        }

        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.chatMessageRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        MessageAdapter adapter = new MessageAdapter(context, message);
        recyclerView.setAdapter(adapter);

        return v;
    }
}
