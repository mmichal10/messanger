package com.example.michal.komunikator;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Comparator;

/**
 * Created by michal on 10.04.2017.
 */

public class Gesture implements Parcelable {

    private int id = -1;
    private String fileName = null;
    private String name = null;
    private Drawable pic = null;
    private String category = "not assigned";

    public Gesture(String fileName, String name, Drawable pic, int id, String category){
        this.name = name;
        this.pic = pic;
        this.category = category;
        this.id = id;
        this.fileName = fileName;
    }

    public Gesture(Parcel in){
        this.id = in.readInt();
        this.name = in.readString();
        this.category = in.readString();
        this.pic = (Drawable) in.readValue(getClass().getClassLoader());
        this.fileName = in.readString();
    }


    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public Drawable getSymbol() {
        return pic;
    }

    public String getCategory(){
        return category;
    }

    public String getFileName(){
        return  fileName;
    }

    public static class GestureNameComparator implements Comparator<Gesture> {

        @Override
        public int compare(Gesture o1, Gesture o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    }

    //two methods from Parcelable interface
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.category);
        Bitmap bitmap = ((BitmapDrawable) this.pic).getBitmap();
        dest.writeParcelable(bitmap, flags);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public Gesture createFromParcel(Parcel in){
            return new Gesture(in);
        }

        public Gesture[] newArray(int size){
            return new Gesture[size];
        }
    };

    @Override
    public String toString(){
        return this.fileName;
    }

}
