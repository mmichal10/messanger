package com.example.michal.komunikator.Gallery.gesturesLists;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.example.michal.komunikator.Constants;
import com.example.michal.komunikator.Gesture;
import com.example.michal.komunikator.Gallery.gesturesLists.byName.GesturesByNameFragment;
import com.example.michal.komunikator.Gallery.gesturesLists.bySections.GestureBySectionsFragment;
import com.example.michal.komunikator.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class GalleryActivity extends FragmentActivity {

    private ArrayList<Gesture> mGesturesList = null;
    private ArrayList<String> mSectionsList = null;
    private EditText mFinder = null;
    private GesturesByNameFragment mLexigraphicalFragment = null;
    private GestureBySectionsFragment mSectionsFragment = null;
    private FrameLayout mFragmentContainer = null;
    private Spinner mOrderSpinner = null;
    private ArrayList<String> mMessageList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        mFragmentContainer = (FrameLayout)findViewById(R.id.fragmentFrame);

        readDrawable();
        setContent();
    }

    private void readDrawable(){
        mGesturesList = new ArrayList<>();
        mSectionsList = new ArrayList<>();

        ArrayList<Field> idFields = new ArrayList<>(Arrays.asList(R.drawable.class.getFields()));

        for(Field field : idFields) {
            String fileName = field.getName();
            if (fileName.startsWith(Constants.PREFIX))
                try {
                    int id = field.getInt(null);
                    Gesture g = new Gesture(fileName, id);
                    mGesturesList.add(g);

                    if (!mSectionsList.contains(g.getCategory()))
                        mSectionsList.add(g.getCategory());

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
        }
    }

    private void setContent(){
        mMessageList = new ArrayList<>();

        Bundle args = new Bundle();
        args.putParcelableArrayList("gestures", mGesturesList);
        mLexigraphicalFragment = new GesturesByNameFragment();
        mLexigraphicalFragment.setArguments(args);

        args.putStringArrayList("sections", mSectionsList);
        mSectionsFragment = new GestureBySectionsFragment();
        mSectionsFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentFrame, mLexigraphicalFragment).commit();

        mFinder = (EditText)findViewById(R.id.findField);
        mFinder.addTextChangedListener(searchWatecher);

        mOrderSpinner = (Spinner)findViewById(R.id.spinner);
        mOrderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if(id == 0)
                    transaction.replace(R.id.fragmentFrame, mLexigraphicalFragment);
                else if(id == 1)
                    transaction.replace(R.id.fragmentFrame, mSectionsFragment);
                mFinder.setText("");
                transaction.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private final TextWatcher searchWatecher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mLexigraphicalFragment.getFilter().filter(s.toString());
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void afterTextChanged(Editable s) {}
    };

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra("MESSAGE", mMessageList);
        setResult(Constants.CHOOSEN_GESTURES_LIST_REQUEST, intent);
        finish();
    }

    public void addGestureToMessege(Gesture g){
        mMessageList.add(g.getFileName());
    }

}
