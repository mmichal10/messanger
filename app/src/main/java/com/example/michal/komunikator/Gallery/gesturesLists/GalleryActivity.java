package com.example.michal.komunikator.Gallery.gesturesLists;

import android.content.Intent;
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

import com.example.michal.komunikator.Gesture;
import com.example.michal.komunikator.Gallery.gesturesLists.byName.GesturesByNameFragment;
import com.example.michal.komunikator.Gallery.gesturesLists.bySections.GestureBySectionsFragment;
import com.example.michal.komunikator.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class GalleryActivity extends FragmentActivity {


    private static final String PREFIX = "makaton_";
    static final int CHOOSEN_GESTURES_LIST_REQUEST = 1;


    private ArrayList<Gesture> gesturesList = null;
    private ArrayList<String> sectionsList = null;
    private EditText finder = null;
    private GesturesByNameFragment lexigraphicalFragment = null;
    private GestureBySectionsFragment sectionsFragment = null;
    private FrameLayout fragmentContainer = null;
    private Spinner orderSpinner = null;
    private ArrayList<String> messageList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        fragmentContainer = (FrameLayout)findViewById(R.id.fragmentFrame);

        readDrawable();
        setContent();

    }

    private void readDrawable(){
        gesturesList = new ArrayList<>();
        sectionsList = new ArrayList<>();


        Field[] ID_Fields = R.drawable.class.getFields();
        for(Field field : ID_Fields) {
            String fileName = field.getName();
            if (fileName.startsWith(PREFIX))
                try {
                    int id = field.getInt(null);
                    //zakładam, że nazwa obrazka to PREFIX_NAZWA_KATEGORIA
                    //najpierw pozbywam sie prefiksu
                    String name = fileName.substring((PREFIX.length()));
                    //wyłuskuję kategorię
                    String category = name.substring(name.indexOf("_") + 1);
                    //i pozbywam się się kategorii z nazwy
                    name = name.substring(0, name.indexOf("_"));
                    Drawable drawable = ResourcesCompat.getDrawable(getResources(), id, null);

                    if (!sectionsList.contains(category))
                        sectionsList.add(category);

                    gesturesList.add(new Gesture(fileName, name, drawable, id, category));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
        }
    }

    private void setContent(){
        messageList = new ArrayList<>();

        Bundle args = new Bundle();
        args.putParcelableArrayList("gestures", gesturesList);
        lexigraphicalFragment = new GesturesByNameFragment();
        lexigraphicalFragment.setArguments(args);

        args.putStringArrayList("sections", sectionsList);
        sectionsFragment = new GestureBySectionsFragment();
        sectionsFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentFrame, lexigraphicalFragment).commit();

        finder = (EditText)findViewById(R.id.findField);
        finder.addTextChangedListener(searchWatecher);

        orderSpinner = (Spinner)findViewById(R.id.spinner);
        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if(id == 0)
                    transaction.replace(R.id.fragmentFrame, lexigraphicalFragment);
                else if(id == 1)
                    transaction.replace(R.id.fragmentFrame, sectionsFragment);
                finder.setText("");
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
            lexigraphicalFragment.getFilter().filter(s.toString());
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void afterTextChanged(Editable s) {}
    };

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra("MESSAGE", messageList);
        setResult(CHOOSEN_GESTURES_LIST_REQUEST, intent);
        finish();
    }

    public void addGestureToMessege(Gesture g){
        messageList.add(g.getFileName());
    }

}
