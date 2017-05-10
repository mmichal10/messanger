package com.example.michal.komunikator.messenger;

import com.example.michal.komunikator.Gesture;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by michal on 29.04.2017.
 */

public class Message {
    private String sender;
    private int messageLength;
    private ArrayList<String> message;

    public Message(String src, ArrayList<Gesture> message){
        this.sender = src;
        this.messageLength = message.size();
        this.message = new ArrayList<>();
        for (Gesture g: message) {
            this.message.add(g.getFileName());
        }
    }

    public ArrayList<String> getMessage(){
        return this.message;
    }

    @Override
    public String toString(){
        String object = "Message[sender=" + this.sender + " messageLength=" + this.messageLength + " message=[";
        return object;
    }
}
