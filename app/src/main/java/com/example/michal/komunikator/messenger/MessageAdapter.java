package com.example.michal.komunikator.messenger;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.michal.komunikator.Gesture;
import com.example.michal.komunikator.R;

import java.util.ArrayList;

/**
 * Created by michal on 15.04.2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail = null;
        public ViewHolder(View view){
            super(view);
            thumbnail = (ImageView)view.findViewById(R.id.gestureThumbnail);
        }
    }

    private Context context = null;
    private ArrayList<Gesture> messageList = null;

    public MessageAdapter(Context context, ArrayList<Gesture> messageList){
        this.context = context;
        this.messageList = messageList;
    }


    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Gesture gesture = messageList.get(position);
        holder.thumbnail.setImageDrawable(gesture.getSymbol());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
