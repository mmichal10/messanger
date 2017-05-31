package com.example.michal.komunikator;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.res.ResourcesCompat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;

import static android.R.attr.id;

/**
 * Created by michal on 10.04.2017.
 */

public class Gesture implements Parcelable {

    private static final int CATEGORY = 1;
    private static final int NAME = 2;

    private int mId = -1;
    private String mFileName = null;

    public Gesture(String fileName, int id){
        this.mId = id;
        this.mFileName = fileName;
    }

    public Gesture(Parcel in){
        this.mId = in.readInt();
        this.mFileName = in.readString();
    }


    private String getFileNamePart(int part){
        //nazwa obrazka ma format prefiks_nazwa_kategoria
        //ta funkcja ma wyłuskać, to co potrzebne w danym momencie
        String name = mFileName.substring((Constants.PREFIX.length()));

        String category = name.substring(name.indexOf("_") + 1);
        name = name.substring(0, name.indexOf("_"));
        switch (part){
            case NAME:
                return name;
            case CATEGORY:
                return category;
            default:
                return "Default Name";
        }
    }

    public String getName(){
        return getFileNamePart(NAME);
    }

    public Drawable getSymbol() {
        return ResourcesCompat.getDrawable(App.context().getResources(), mId, null);
    }

    public String getCategory(){
        return getFileNamePart(CATEGORY);
    }

    public String getFileName(){
        return  mFileName;
    }

    public static class GestureNameComparator implements Comparator<Gesture> {

        @Override
        public int compare(Gesture o1, Gesture o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    }

    @Override
    public String toString(){
        return this.mFileName;
    }

    //two methods from Parcelable interface
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mFileName);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public Gesture createFromParcel(Parcel in){
            return new Gesture(in);
        }

        public Gesture[] newArray(int size){
            return new Gesture[size];
        }
    };

    public static ArrayList<Gesture> findDrawableByName(ArrayList<String> gesturesNames){
        ArrayList<Gesture> result = new ArrayList<>();

        for (String fileName: gesturesNames) {
            Field field;
            try {
                field = R.drawable.class.getField(fileName);
                int id = field.getInt(null);
                result.add(new Gesture(fileName, id));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }



}
